package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 * @author CL
 * @date 2020/5/27
 */
public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {

    /**
     * Find first item order by id desc
     * @return financial account
     */
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    FinancialAccount findFirstOrderByIdDesc();
}
