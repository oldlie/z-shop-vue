package com.oldlie.zshop.zshopvue.model.db;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商品套餐
 */
@Entity
@Table(name = "t_commodity_formula")
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CommodityFormula extends BaseEO implements Serializable {
    private Long commodityId;
    private String title;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
         parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
    private int inventory;
}
