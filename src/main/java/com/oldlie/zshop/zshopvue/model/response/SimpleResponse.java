package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

/**
 * @author oldlie
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SimpleResponse<T> extends BaseResponse {
    private T item;

    @Override
    public SimpleResponse<T> failed(String message) {
        super.failed(message);
        return this;
    }
}
