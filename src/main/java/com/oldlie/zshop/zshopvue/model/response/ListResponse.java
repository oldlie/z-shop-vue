package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ListResponse<T> extends BaseResponse {
    private List<T> list;
}
