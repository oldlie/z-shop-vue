package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oldlie
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_shopping_order_item")
@ToString
public class ShoppingOrderItem extends BaseEO {
    private Long shoppingOrderId;
    private Long commodityId;
    private String commodityTitle;
    private String commodityImage;
    private Long formulaId;
    private String formulaTitle;
    private int formulaCount;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
         parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
    @Column(columnDefinition = "tinyint default 0 comment '订单中的某件商品的状态,0未评论；1已评论'")
    private int status;
}
