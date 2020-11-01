package com.oldlie.zshop.zshopvue.service.init.permission;

import com.oldlie.zshop.zshopvue.model.cs.Csp;
import com.oldlie.zshop.zshopvue.model.db.Permission;
import com.oldlie.zshop.zshopvue.model.db.PermissionType;
import com.oldlie.zshop.zshopvue.service.init.InitBase;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class FrontendPermission extends InitBase<Permission> {

    private final Permission permission;

    @Override
    public Permission getEntity() {
        return this.permission;
    }

    public FrontendPermission() {
        this.permission = new Permission();
        this.permission.setParentId(0);
        this.permission.setKey(Csp.MODULE + "2");
        this.permission.setType(PermissionType.URL.getCode());
        this.permission.setUrl("/frontend/**");
        this.permission.setTitle("前端API URL");
        this.permission.setComment("前期处理的比较粗狂，前端API的URL都打包进来了");
    }
}
