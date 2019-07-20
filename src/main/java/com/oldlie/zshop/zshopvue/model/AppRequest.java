package com.oldlie.zshop.zshopvue.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppRequest<T> implements Serializable {
    private T body;
}
