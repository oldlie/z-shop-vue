package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_shopping_order")
@ToString
public class ShoppingOrder extends BaseEO {
    private Long uid;
    private String serialNumber;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
         parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money totalMoney;
    @Column(columnDefinition = "tinyint comment '订单状态'")
    private int status;
    @Column(columnDefinition = "varchar(1000) comment '格式化之后的地址信息'")
    private String addressInfo;
}
