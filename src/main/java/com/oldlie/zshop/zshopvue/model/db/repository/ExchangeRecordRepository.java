package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ExchangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author CL
 * @date 2020/5/25
 */
public interface ExchangeRecordRepository extends JpaRepository<ExchangeRecord, Long>,
        JpaSpecificationExecutor<ExchangeRecord> {
}
