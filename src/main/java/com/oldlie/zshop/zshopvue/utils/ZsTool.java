package com.oldlie.zshop.zshopvue.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ZsTool {

    public static Sort sort(String orderBy, String order) {
        return order == null || order.toLowerCase().equals("desc") ?
                new Sort(Sort.Direction.DESC, orderBy) : new Sort(Sort.Direction.ASC, orderBy);
    }

    /**
     * Get Pageable object
     * @param page page index start with 1
     * @param size page size
     * @param orderBy field
     * @param order direct
     * @return pageable object
     */
    public static Pageable pageable(int page, int size, String orderBy, String order) {
        int start = page <= 1 ? 0 : page - 1;
        return PageRequest.of(start, size, ZsTool.sort(orderBy, order));
    }

    /**
     * Get list by id desc
     * @param page page index
     * @param size page size
     * @return pageable
     */
    public static Pageable pageable(int page, int size) {
        return pageable(page, size, "id", "desc");
    }
}
