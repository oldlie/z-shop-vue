package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 这个类用于记录已用户对商品的每一次打分
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_user_commodity_eva")
@ToString
public class UserCommodityEva extends BaseEO {
    private long uid;
    /**
     * 商品ID
     */
    private long cid;
    /**
     * 商品名称
     */
    private String comTitle;
    /**
     * 维度ID
     */
    private long eid;
    /**
     * 维度
     */
    private String evaText;
    /**
     * 订单ID
     */
    private long oid;
    private int count;
}
