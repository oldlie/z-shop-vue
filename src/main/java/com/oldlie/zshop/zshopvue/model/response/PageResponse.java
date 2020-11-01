package com.oldlie.zshop.zshopvue.model.response;

import lombok.*;

import java.util.List;

/**
 * @author oldlie
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PageResponse<T> extends ListResponse<T> {
    private long total;

    @Override
    public PageResponse<T> failed(String message) {
        super.failed(message);
        return this;
    }
}
