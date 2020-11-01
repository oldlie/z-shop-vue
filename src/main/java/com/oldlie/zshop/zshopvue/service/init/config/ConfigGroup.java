package com.oldlie.zshop.zshopvue.service.init.config;

import lombok.Getter;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public enum ConfigGroup {

    /**
     * 短信设置
     */
    SMS(1, "短信配置");

    @Getter
    private int gid;
    @Getter
    private String group;

    ConfigGroup(int gid, String group) {
        this.gid = gid;
        this.group = group;
    }
}
