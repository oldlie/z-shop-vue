package com.oldlie.zshop.zshopvue.exception;

import com.oldlie.zshop.zshopvue.model.cs.HTTP;
import lombok.Getter;

public class AppRestException extends Exception {

    @Getter
    private int code;

    public String getLocalizedMessage() {
        return HTTP.localizationMessage(code);
    }

    public AppRestException(String message) {
        super(message);
    }

    public AppRestException(String message, int code) {
        super(message);
        this.code = code;
    }
}
