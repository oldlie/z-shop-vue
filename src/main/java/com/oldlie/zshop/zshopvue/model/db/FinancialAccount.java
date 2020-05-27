package com.oldlie.zshop.zshopvue.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;

/**
 * 本系统的金融账号，用户系统的金融流水
 * @author CL
 * @date 2020/5/27
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "t_financial_account")
public class FinancialAccount extends BaseEO {
    /**
     * 触发操作的用户ID
     */
    private long optUid;
    public static final int INCOME = 1;
    public static final int EXPENSE = -1;
    /**
     * 账户是否是增加金额：-1 支出；1 收入；
     */
    private int isAdd;
    /**
     * 本次操作的金额
     */
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money money;
    /**
     * 账户的余额
     */
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money balance;
    private String comment;
}
