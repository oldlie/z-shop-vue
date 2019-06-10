package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
public class BaseResponse {
    private int status = 0;
    private String message = "SUCCESS";
}
