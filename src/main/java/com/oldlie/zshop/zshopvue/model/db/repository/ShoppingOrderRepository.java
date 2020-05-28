package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 */
public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Long>,
        JpaSpecificationExecutor<ShoppingOrder> {

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

    /**
     * Find user's shopping order history
     * @param uid user id
     * @param pageable pageable information
     * @return shopping order list
     */
    Page<ShoppingOrder> findAllByUid(long uid, Pageable pageable);
}
