package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.PayCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 通过用户的id查找系统管理员分配给他的兑换卡
     * @param uid customer user id
     * @return Page of Object[PayCard, UserCard]
     */
    @Query("FROM PayCard p JOIN UserCard u on p.id=u.cardId WHERE u.uid=:uid AND p.isExchanged=:isExchanged ORDER BY p.id DESC")
    Page<Object> findAllCustomerValidCards(@Param("uid")long uid,
                                           @Param("isExchanged") int isExchanged,
                                           Pageable pageable);
}
