package com.oldlie.zshop.zshopvue.model.response.wangeditor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode
@ToString
public class ImageResponse {
    private int errno;
    private List<String> data;
}
