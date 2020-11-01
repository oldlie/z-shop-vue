package com.oldlie.zshop.zshopvue.service.init.permission;

import com.oldlie.zshop.zshopvue.model.db.Permission;
import com.oldlie.zshop.zshopvue.model.db.repository.PermissionRepository;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/11/1
 */
@Service
@Slf4j
public class InitPermissionService extends InitService<Permission, InitBase<Permission>, Long, PermissionRepository> {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void init() {
        this.init(new BackendPermission()).init(new FrontendPermission());
    }

    @Override
    public void setRepository() {
        this.repository = this.permissionRepository;
    }
}
