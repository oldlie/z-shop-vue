package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author CL
 * @date 2020/5/26
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    /**
     * Find one by user id, commodity id and formula id
     * @param uid user id
     * @param cid commodity id
     * @param fid formula id
     * @return shopping cart
     */
    ShoppingCart findOneByUidAndCommodityIdAndFormulaId(long uid, long cid, long fid);

    /**
     * Delete by user id, commodity id and formula id
     * @param uid user id
     * @param cid commodity id
     * @param fid formula id
     */
    void deleteByUidAndCommodityIdAndFormulaId(long uid, long cid, long fid);

    /**
     * Find all by user id
     * @param uid user id
     * @param pageable pagination information
     * @return shopping cart items
     */
    Page<ShoppingCart> findAllByUid(long uid, Pageable pageable);
}
