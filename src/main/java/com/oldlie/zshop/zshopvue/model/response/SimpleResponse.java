package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SimpleResponse<T> extends BaseResponse {
    private T item;
}
