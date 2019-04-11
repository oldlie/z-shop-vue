package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.UrlRoleMapping;
import com.oldlie.zshop.zshopvue.model.db.UrlRoleMappingRepository;
import com.oldlie.zshop.zshopvue.model.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

    public UserService(UserRepository userRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository) {
        this.userRepository = userRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findFirstByUsername(s);
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
