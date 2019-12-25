package com.oldlie.zshop.zshopvue.controller;

import com.oldlie.zshop.zshopvue.exception.AppRestException;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AppRestException.class)
    public BaseResponse exceptionHandle(AppRestException e) {
        BaseResponse response = new BaseResponse();
        response.setStatus(e.getCode());
        response.setMessage(e.getLocalizedMessage());
        return response;
    }
}
