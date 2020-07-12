package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.FinancialAccount;
import com.oldlie.zshop.zshopvue.model.db.repository.FinancialAccountRepository;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/7/12
 */
@Service
@Slf4j
public class FinancialAccountService {

    @Autowired
    private FinancialAccountRepository faRepository;

    /**
     * 获取系统的资金流水，确切流水应该已每项记录结果累加为准，每项中的余额仅作参考
     * @param index page index
     * @param size page szie
     * @return Page of FinancialAccount
     */
    public PageResponse<FinancialAccount> list(int index, int size) {
        PageResponse<FinancialAccount> response = new PageResponse<>();
        Page<FinancialAccount> page = this.faRepository.findAll(ZsTool.pageable(index, size));
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }
}
