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
@Table(name = "t_carousel")
@ToString
public class Carousel extends BaseEO {
    private String title;
    private String summary;
    private String url;
    private String imageUrl;
    private int itemOrder;
}
