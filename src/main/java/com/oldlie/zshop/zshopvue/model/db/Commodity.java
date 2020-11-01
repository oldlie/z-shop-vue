package com.oldlie.zshop.zshopvue.model.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * 仅用于存储商品概览信息，商品详细信息尽量存在 commodity profile 里
 */
@Entity
@Table(name = "t_commodity")
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class Commodity extends BaseEo {
    private String title;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '商品简介'")
    private String introduction;
    private String thumbnail;
    private int likeCount;
    private int shareCount;
    private int viewCount;
    @Column(columnDefinition = "tinyint COMMENT '商品状态'")
    private int status;
    @Column(columnDefinition = "VARCHAR(1000) DEFAULT '' COMMENT '二维码，引导用户去手机端现金购买'")
    private String qrCode;
    /**
     * 销售数量
     */
    private int sellCount;
    /**
     * 点评次数
     */
    private int commentCount;
    /**
     * 点评总分
     */
    private long commentScore;
}
