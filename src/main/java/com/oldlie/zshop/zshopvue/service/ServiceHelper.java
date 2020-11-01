package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.repository.BaseRepository;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class ServiceHelper<T, ID, R extends BaseRepository<T, ID>> {

    R repository;

    public ServiceHelper(R repository) {
        this.repository = repository;
    }

    public static <T, ID, R extends BaseRepository<T, ID>> ServiceHelper<T, ID, R> getInstance(R r) {
        return new ServiceHelper<>(r);
    }
}
