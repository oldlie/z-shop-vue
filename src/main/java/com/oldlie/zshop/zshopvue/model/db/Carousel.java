package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_carousel")
@ToString
public class Carousel extends BaseEo {
    @NotEmpty
    @Length(max = 32, message = "标题最多包含32字符")
    private String title;
    @Length(max = 120, message = "简介最多包含120字符")
    private String summary;
    @NotEmpty
    @Length(max = 255, message = "URL最多255字符")
    private String url;
    @NotEmpty
    @Length(max = 255, message = "图片本身的URL最多255字符")
    private String imageUrl;
    private int itemOrder;
}
