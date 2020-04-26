package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_carousel_nav")
@ToString
public class CarouselNavigation extends BaseEO {
    @NotNull
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "tag_id")
    private Tag tag;
    // 序号，决定排序
    private int serial;
}
