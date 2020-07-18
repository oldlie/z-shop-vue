package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品的用户评价
 * @author oldlie
 * @date 2020/7/18
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_commodity_comment")
@ToString
public class CommodityComment extends BaseEO {
    private long uid;
    private String nickname;
    private long cid;
    @Column(columnDefinition = "VARCHAR(1000) DEFAULT '' COMMENT '用于存储用户的打分记录，直接用json'")
    private String evaDim;
    /**
     * 用户的文字评价
     */
    private String evaText;
    /**
     * 用户上传最多6张图片，注意删除评论的时候也别忘了删这6张图
     */
    private String images;
    /**
     * 点赞数
     */
    private long agree;
    /**
     * 讨厌数
     */
    private long disagree;
}
