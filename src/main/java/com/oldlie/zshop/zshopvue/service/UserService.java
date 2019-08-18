package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.UrlRoleMapping;
import com.oldlie.zshop.zshopvue.model.db.repository.UrlRoleMappingRepository;
import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

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
                String value = urlRoleMap.get(x.getUrl()) + ",ROLE_" + x.getRole();
                urlRoleMap.put(x.getUrl(), value);
            } else {
                urlRoleMap.put(x.getUrl(), "ROLE_" + x.getRole());
            }
        });
        return urlRoleMap;
    }
}
