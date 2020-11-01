package com.oldlie.zshop.zshopvue.model.response;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import lombok.*;

import java.util.List;

/**
 * @author oldlie
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ListResponse<T> extends BaseResponse {
    protected List<T> list;

    @Override
    public ListResponse<T> failed(String message) {
        this.status = HTTP_CODE.FAILED;
        this.message = message;
        return this;
    }
}
