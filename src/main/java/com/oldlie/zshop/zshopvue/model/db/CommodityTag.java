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
@SecondaryTable(
        name = "t_tag",
        pkJoinColumns = { @PrimaryKeyJoinColumn(name = "id") }
)
@ToString
public class CommodityTag extends BaseEO{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long commodityId;
    @Column(name = "tag_id", table = "t_tag")
    private Long tagId;
    @Column(name = "title", table = "t_tag")
    private String title;
}
