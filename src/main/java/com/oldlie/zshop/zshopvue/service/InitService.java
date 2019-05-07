package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━━━━━━━
 * 2019/04/11
 * InitService
 *
 * @author 陈列
 */
@Service
public class InitService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

    public InitService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository,
                       UserRepository userRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
    }

    public void init() {
        this.initUsers();
        this.initUrlRoleMapping();
    }

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
                            .name("ADMIN")
                            .title("管理员")
                            .build());
        }
        User adminUser = this.userRepository.findFirstByUsername("admin");
        if (adminUser == null) {
            this.userRepository.save(User.builder()
                    .username("admin")
                    .password(this.bCryptPasswordEncoder.encode("admin"))
                    .roles(Arrays.asList(role, userRole))
                    .isEnabled(true)
                    .build());
        }
        User normalUser = this.userRepository.findFirstByUsername("user");
        if (normalUser == null) {
            this.userRepository.save(User.builder()
                    .username("user")
                    .password(this.bCryptPasswordEncoder.encode("user@123"))
                    .roles(Arrays.asList(role, userRole))
                    .isEnabled(true)
                    .build());
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
