package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 首页需要展示的商品标签
 * 这个信息以后需要加到缓存里
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_home_tag",
        uniqueConstraints = @UniqueConstraint(columnNames = { "tagId" }))
@ToString
public class HomeTag extends BaseEO {
    private long tagId;
    private int sequence;
    @Column(columnDefinition = "INT DEFAULT 0 COMMENT 'tag类别'")
    private int category;
}
