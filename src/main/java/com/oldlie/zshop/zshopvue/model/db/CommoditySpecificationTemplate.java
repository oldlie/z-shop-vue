package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_commodity_spec_temp")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommoditySpecificationTemplate extends BaseEo implements Serializable {
    private String title;
    @Column(columnDefinition = "VARCHAR(4000) COMMENT '规格模板内容'")
    private String template;
}
