package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品评价维度，每件商品最多设置5个
 * @author oldlie
 * @date 2020/7/18
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_eva_dim")
@ToString
public class EvaluativeDimension extends BaseEo {
    /**
     * 商品ID
     */
    private long cid;
    private String title;
    /**
     * 评价总次数
     */
    private long count;
    /**
     * 评价总分
     */
    private long total;
}
