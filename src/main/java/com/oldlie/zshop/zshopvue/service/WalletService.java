package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.Wallet;
import com.oldlie.zshop.zshopvue.model.db.repository.WalletRepository;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户余额信息
 * @author CL
 * @date 2020/5/25
 */
@Service
@Slf4j
public class WalletService {

    @Autowired
    private WalletRepository repository;

    /**
     * 后台查看列表用的
     * @param pageIndex
     * @param size
     * @param orderBy
     * @param direct
     * @return
     */
    public PageResponse<Wallet> page(int pageIndex, int size, String orderBy, String direct) {
        PageResponse<Wallet> response = new PageResponse<>();
        return response;
    }

}
