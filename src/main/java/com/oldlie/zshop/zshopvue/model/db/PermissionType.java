package com.oldlie.zshop.zshopvue.model.db;

import lombok.Getter;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public enum PermissionType {

    /**
     * Permission Type
     */
    URL(0, "URL"),
    UI(1, "UI");

    @Getter
    private int code;
    @Getter
    private String title;

    PermissionType(int code, String title) {
        this.code = code;
        this.title = title;
    }
}
