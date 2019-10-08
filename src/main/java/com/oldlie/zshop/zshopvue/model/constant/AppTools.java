package com.oldlie.zshop.zshopvue.model.constant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class AppTools {

    public static Sort sort(String direct, String field) {
        return new Sort(direct.toUpperCase().equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, field);
    }

    public static Pageable pageable(int page, int size, String field, String direct) {
        int p = page >= 1 ? page - 1 : 0;
        return PageRequest.of(p, size, sort(direct, field));
    }
}
