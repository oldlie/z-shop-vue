package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PageResponse<T> extends ListResponse<T> {
    private long total;
}
