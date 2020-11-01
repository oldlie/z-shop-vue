package com.oldlie.zshop.zshopvue.service.init.permission;

import com.oldlie.zshop.zshopvue.model.cs.Csp;
import com.oldlie.zshop.zshopvue.model.db.Permission;
import com.oldlie.zshop.zshopvue.model.db.PermissionType;
import com.oldlie.zshop.zshopvue.service.init.InitBase;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class BackendPermission extends InitBase<Permission> {

    private final Permission permission;

    @Override
    public Permission getEntity() {
        return this.permission;
    }

    public BackendPermission() {
        this.permission = new Permission();
        this.permission.setParentId(0);
        this.permission.setKey(Csp.MODULE + "1");
        this.permission.setType(PermissionType.URL.getCode());
        this.permission.setUrl("/backend/**");
        this.permission.setTitle("后台API URL");
        this.permission.setComment("前期处理的比较粗狂，后台API的URL都打包进来了");
    }
}
