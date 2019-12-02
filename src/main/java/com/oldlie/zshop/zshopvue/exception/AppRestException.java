package com.oldlie.zshop.zshopvue.exception;

import com.oldlie.zshop.zshopvue.model.constant.ResponseCode;
import lombok.Getter;

public class AppRestException extends Exception {

    @Getter
    private int code;

    public String getLocalizedMessage() {
        return ResponseCode.localizationMessage(code);
    }

    public AppRestException(String message) {
        super(message);
    }

    public AppRestException(String message, int code) {
        super(message);
        this.code = code;
    }
}
