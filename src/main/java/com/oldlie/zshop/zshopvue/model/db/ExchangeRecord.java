package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author CL
 * @date 2020/5/25
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_exchange_record")
@ToString
public class ExchangeRecord extends BaseEO {
    private long uid;
    /**
     * 关联ID
     * 类别   1    购物消费时，这里关联订单ID
     * 类别   1001    兑换卡充值时，这里关联兑换卡ID
     * 类别   1002    管理员充值时，这里关联管理员UID
     */
    private long correlationId;
    /**
     * 余额表动类别：
     * 0  消费；
     * 1    购物消费
     * 1000 充值；
     * 1001 兑换卡充值
     * 1002 管理员充值
     */
    private int category;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money exchangeMoney;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money balance;
    /**
     * 备注，格式化消息，简明扼要
     * 2020/5/1 8:08 购物消费 1000.00，订单流水 SN2020050100000001
     * 2020/5/1 8:08 兑换卡充值 100.00 兑换卡 SN202005088888888
     * 2020/5/1 8:08 管理员充值 1000.00 管理员：admin
     */
    private String comment;
}
