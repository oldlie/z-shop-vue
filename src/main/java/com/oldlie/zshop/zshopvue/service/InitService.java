package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.db.specification.KeyValueSpecification;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.config.InitConfigService;
import com.oldlie.zshop.zshopvue.service.init.permission.BackendPermission;
import com.oldlie.zshop.zshopvue.service.init.permission.FrontendPermission;
import com.oldlie.zshop.zshopvue.service.init.permission.InitPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private InitPermissionService initPermissionService;

    @Autowired
    private PermissionRepository permissionRepository;
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
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
        this.initConfigService.init();
        this.initPermissionService.init();
        this.initUsers();
        this.initAdminPermission();
        this.initUserPermission();
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

    private void initAdminPermission() {
        List<InitBase<Permission>> list = new LinkedList<>();
        list.add(new BackendPermission());
        list.add(new FrontendPermission());

        this.initPermission("ADMIN", list);
    }

    private void initUserPermission() {
        List<InitBase<Permission>> list = new LinkedList<>();
        list.add(new FrontendPermission());
        this.initPermission("USER", list);
    }

    private void initPermission(String roleName, List<InitBase<Permission>> list) {
        Role role = this.roleRepository.findFirstByName(roleName);
        if (role == null) {
            throw new RuntimeException(roleName + " ROLE is null");
        }

        list.forEach(x -> {
            Optional<Permission> optional = this.permissionRepository
                    .findOne(KeyValueSpecification.getInstance(KeyEntity.KEY, x.getEntity().getKey()));
            if (!optional.isPresent()) {
                throw new RuntimeException(x.getEntity().getKey() + "is not initialized");
            }
            Permission permission = optional.get();
            Optional<RolePermission> optional1 = this.rolePermissionRepository
                    .findOne((root, criteriaQuery, criteriaBuilder) -> {
                        Predicate predicate = criteriaBuilder.equal(root.get(RolePermission.RID), role.getId());
                        Predicate predicate1 = criteriaBuilder.equal(root.get(RolePermission.PID), permission.getId());
                        return criteriaBuilder.and(predicate, predicate1);
                    });
            if (!optional1.isPresent()) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPid(permission.getId());
                rolePermission.setRid(role.getId());
                rolePermission.setRole(role.getName());
                this.rolePermissionRepository.save(rolePermission);
            }
        });
    }
}
