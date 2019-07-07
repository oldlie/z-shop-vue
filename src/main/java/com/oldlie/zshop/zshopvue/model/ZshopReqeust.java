package com.oldlie.zshop.zshopvue.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZshopReqeust <T> implements Serializable {
    private T body;
}
