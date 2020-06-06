package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author CL
 * @date 2020/5/26
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name = "t_shopping_order_serial")
@ToString
public class ShoppingOrderSerial extends BaseEO {
    private int year;
    private int month;
    private int date;
    private int ymd;
    @Column(columnDefinition = "INT DEFAULT 1 COMMENT '每天流水计数'")
    private int count;
}
