package com.oldlie.zshop.zshopvue.service.init;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public abstract class InitBase<T> {

    /**
     * 子类必须实现
     * @return T
     */
    public abstract T getEntity();
}
