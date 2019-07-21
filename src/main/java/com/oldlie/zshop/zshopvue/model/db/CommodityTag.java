package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_commodity_tag")
@ToString
public class CommodityTag extends BaseEO{
    private Long commodityId;
    private Long tagId;
}
