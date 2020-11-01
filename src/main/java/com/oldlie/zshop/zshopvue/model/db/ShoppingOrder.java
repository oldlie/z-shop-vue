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
public class ShoppingOrder extends BaseEo {
    private Long uid;
    private String serialNumber;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
         parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money totalMoney;
    /**
     * 注意这个状态，从待评价修改为已完成要求所有的item都已经评价了
     */
    @Column(columnDefinition = "tinyint comment '订单状态'")
    private int status;
    @Column(columnDefinition = "varchar(2000) comment '格式化之后的地址信息'")
    private String addressInfo;
    @Column(columnDefinition = "varchar(255) comment '快递单号'")
    private String postSerialNumber;
    @Column(columnDefinition = "varchar(255) comment '快递公司'")
    private String postCompany;
    @Column(columnDefinition = "varchar(1000) comment '订单备注信息'")
    private String comment;
    @Column(columnDefinition = "varchar(1000) comment '订单取消备注'")
    private String cancelReason;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ShoppingOrderId")
    private Collection<ShoppingOrderItem> items;
}
