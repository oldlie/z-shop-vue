package com.oldlie.zshop.zshopvue.model.response;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import lombok.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author oldlie
 */
@Data
@EqualsAndHashCode
@ToString
public class BaseResponse implements Response<BaseResponse> {
    protected int status = 0;
    protected String message = "SUCCESS";

    public String getMessage() {
        try {
            return URLEncoder.encode(this.message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return this.message;
        }
    }

    @Override
    public BaseResponse success(String message) {
        this.status = HTTP_CODE.SUCCESS;
        this.message = message;
        return this;
    }

    @Override
    public BaseResponse failed(String message) {
        this.status = HTTP_CODE.FAILED;
        this.message = message;
        return this;
    }
}
