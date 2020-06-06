package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderSerial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

/**
 * @author CL
 * @date 2020/5/26
 */
public interface ShoppingOrderSerialRepository extends JpaRepository<ShoppingOrderSerial, Long> {

    /**
     * find by year,month,date
     * @param ymd year + month + date
     * @return order serial
     */
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    ShoppingOrderSerial findByYmd(int ymd);
}
