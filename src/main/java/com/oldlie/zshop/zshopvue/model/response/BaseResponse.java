package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@EqualsAndHashCode
@ToString
public class BaseResponse {
    private int status = 0;
    private String message = "SUCCESS";

    public String getMessage() {
        try {
            return URLEncoder.encode(this.message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return this.message;
        }
    }
}
