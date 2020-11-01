package com.oldlie.zshop.zshopvue.model.db;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public interface KeyEntity {

    String KEY = "key";

    /**
     * 获取唯一值
     * @return 唯一值
     */
    String getKey();
}
