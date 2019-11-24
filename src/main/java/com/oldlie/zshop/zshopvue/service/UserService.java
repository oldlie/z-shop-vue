package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.Role;
import com.oldlie.zshop.zshopvue.model.db.UrlRoleMapping;
import com.oldlie.zshop.zshopvue.model.db.repository.RoleRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.UrlRoleMappingRepository;
import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.util.UserProfile;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository repository) {
        this.roleRepository = repository;
    }

    public UserService(UserRepository userRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository) {
        this.userRepository = userRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
    }

    public User user(String username) {
        return this.userRepository.findFirstByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails details = this.userRepository.findFirstByUsername(s);
        ((User) details).getRoles().forEach(x -> {
            log.debug(x.getName());
        });
        return details;
    }

    private Map<String, String> urlRoleMap = null;

    public Map<String, String> loadUrlMap() {
        urlRoleMap = new HashMap<>();
        List<UrlRoleMapping> list = this.urlRoleMappingRepository.findAll();
        list.forEach(x -> {
            if (urlRoleMap.keySet().contains(x.getUrl())) {
                String value = urlRoleMap.get(x.getUrl()) + "," + x.getRole();
                urlRoleMap.put(x.getUrl(), value);
            } else {
                urlRoleMap.put(x.getUrl(), x.getRole());
            }
        });
        return urlRoleMap;
    }

    public void loadUrlMap(final Map<String, String> map) {
        List<UrlRoleMapping> list = this.urlRoleMappingRepository.findAll();
        list.forEach(x -> {
            if (map.keySet().contains(x.getUrl())) {
                String value = map.get(x.getUrl()) + ",ROLE_" + x.getRole();
                map.put(x.getUrl(), value);
            } else {
                map.put(x.getUrl(), "ROLE_" + x.getRole());
            }
        });
    }

    public User initUser(final String username, final String password, final String role) {
        Role _role = this.roleRepository.findFirstByName(role);
        if (_role == null) {
            throw new RuntimeException("Role " + role + " does not exist.");
        }
        return this.initUser(username, password, _role);
    }

    public User initUser(final String username, final String password, final Role role) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        User user = User
                .builder()
                .username(username)
                .password(this.bCryptPasswordEncoder.encode(password))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .roles(roles)
                .build();
        user = this.userRepository.save(user);
        return user;
    }
}
