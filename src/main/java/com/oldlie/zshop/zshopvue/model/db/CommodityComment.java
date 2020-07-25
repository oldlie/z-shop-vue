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
    /**
     * 订单ID，每个订单都可以评论
     */
    private long oid;
    /**
     * 订单号
     */
    private String sn;
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
    @Column(columnDefinition = "TINYINT DEFAULT 0 COMMENT '评论状态，默认是0,即不展示；管理员审核之后改为1,即可以在前端展示；管理员审核不通过时设置为2'")
    private int status;
}
