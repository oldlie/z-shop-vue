package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author oldlie
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_shopping_order", uniqueConstraints = { @UniqueConstraint(columnNames = "serialNumber") })
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_order_id")
    private Collection<ShoppingOrderItem> items;
}
