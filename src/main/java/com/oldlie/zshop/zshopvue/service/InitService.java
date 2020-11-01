package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.RoleRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.UrlRoleMappingRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.UserRepository;
import com.oldlie.zshop.zshopvue.service.init.config.InitConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * 2019/04/11
 * InitService
 *
 * @author 陈列
 */
@Service
public class InitService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private InitConfigService initConfigService;

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

    private UserService userService;

    @Autowired
    public void setUserService(UserService service) {
        this.userService = service;
    }

    public InitService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository,
                       UserRepository userRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
    }

    private boolean isInit = false;

    public void init() {
        if (isInit) {
            return;
        }
        this.initUsers();
        this.initUrlRoleMapping();
        this.initConfigService.init();
        this.isInit = true;
    }

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class})
    private void initUsers() {
        Role role = this.roleRepository.findFirstByName("ADMIN");
        if (role == null) {
            role = this.roleRepository.save(
                    Role.builder()
                            .name("ADMIN")
                            .title("管理员")
                            .build());
        }
        Role userRole = this.roleRepository.findFirstByName("USER");
        if (userRole == null) {
            userRole = this.roleRepository.save(
                    Role.builder()
                            .name("USER")
                            .title("注册用户")
                            .build());
        }
        User adminUser = this.userRepository.findFirstByUsername("admin");
        if (adminUser == null) {
            this.userService.initUser("admin", "admin@123", role);
        }
        User normalUser = this.userRepository.findFirstByUsername("user");
        if (normalUser == null) {
            this.userService.initUser("user", "user@123", userRole);
        }
    }

    private void initUrlRoleMapping() {
        if (this.urlRoleMappingRepository.count() > 0){
            return;
        }
        this.urlRoleMappingRepository.saveAll(
                Arrays.asList(
                        UrlRoleMapping.builder().url("/backend/**").role("ADMIN").build(),
                        UrlRoleMapping.builder().url("/front/**").role("ADMIN").build(),
                        UrlRoleMapping.builder().url("/front/**").role("USER").build()
                )
        );
    }
}
