package com.oldlie.zshop.zshopvue.model.response;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public interface Response<T> {
    /**
     * 返回成功的结果
     * @param message message
     * @return success response
     */
    T success(String message);

    /**
     * 返回失败的消息
     * @param message message
     * @return failed response
     */
    T failed(String message);
}
