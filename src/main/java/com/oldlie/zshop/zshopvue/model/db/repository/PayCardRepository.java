package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.PayCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 */
public interface PayCardRepository extends JpaRepository<PayCard, Long>, JpaSpecificationExecutor<PayCard> {

    /**
     * 查找指定用户的所有的兑换卡
     * @param uid user id
     * @param pageable pageable information
     * @return one page of pay cards
     */
    Page<PayCard> findAllByUid(long uid, Pageable pageable);

    /**
     * 通过序列号和验证码获取兑换卡的信息
     * @param sn 序列号
     * @param vc 验证码
     * @return 兑换卡信息
     */
    PayCard findOneBySerialNumberAndVerifyCode(String sn, String vc);
}
