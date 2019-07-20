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
public class Commodity extends BaseEO {
    private String title;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '商品简介'")
    private String introduction;
    private String thumbnail;
    private int likeCount;
    private int shareCount;
    private int viewCount;
    @Column(columnDefinition = "tinyint COMMENT '商品状态'")
    private int status;
}
