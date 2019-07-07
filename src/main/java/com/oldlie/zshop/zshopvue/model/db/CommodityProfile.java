package com.oldlie.zshop.zshopvue.model.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_commodity_profile")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CommodityProfile extends BaseEO {
    @Column(columnDefinition = "VARCHAR(4000) COMMENT 'json格式规格'")
    private String specification;
    @Column(columnDefinition = "VARCHAR(1000) COMMENT '展示图片路径，用英文逗号分隔'")
    private String images;
    @Column(columnDefinition = "VARCHAR(4000) COMMENT '详情'")
    private String detail;
}
