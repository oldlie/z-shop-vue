package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author DELL
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_shopping_cart")
@ToString
public class ShoppingCart extends BaseEO {
    private Long uid;
    private Long commodityId;
    private String commoditySummary;
    private Long formulaId;
    private String formulaTitle;
    private int count;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
         parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
}
