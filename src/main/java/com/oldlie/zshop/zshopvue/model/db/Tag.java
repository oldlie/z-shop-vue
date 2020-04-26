package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_tag")
@ToString
public class Tag extends BaseEO {
    @NotEmpty
    private String title;
    private int category;
    @Column(columnDefinition = "int comment '标签顺序'")
    private int tagOrder;
    @Column(columnDefinition = "int default 0 comment '上级标签'")
    private Long parentId;
    @Column(columnDefinition = "int default 0 comment '子标签数量'")
    private int childCount;
}
