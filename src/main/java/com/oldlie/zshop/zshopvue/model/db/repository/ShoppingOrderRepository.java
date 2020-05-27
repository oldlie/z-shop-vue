package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author oldlie
 */
public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Long> {

    /**
     * find shopping order by serial number
     * @param sn serial number
     * @return shopping order
     */
    ShoppingOrder findBySerialNumber(String sn);

    /**
     * find by user id and serial number
     * @param uid user id
     * @param sn serial number
     * @return shopping order
     */
    ShoppingOrder findByUidAndSerialNumber(long uid, String sn);
}
