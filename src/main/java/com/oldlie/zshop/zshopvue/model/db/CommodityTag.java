package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_commodity_tag")
//@SecondaryTable(
//        name = "t_tag",
//        pkJoinColumns = { @PrimaryKeyJoinColumn(name = "tag_id") }
//)
@ToString
public class CommodityTag extends BaseEo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tagId;
    private Long commodityId;
}
