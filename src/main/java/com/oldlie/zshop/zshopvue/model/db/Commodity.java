package com.oldlie.zshop.zshopvue.model.db;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_commodity")
@Data
@Serialization
@ToString
@EqualsAndHashCode
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '商品简介'")
    private String introduction;
    private String thumbnail;
    private int likeCount;
    private int shareCount;
    private int viewCount;
    @Column(columnDefinition = "tinyint COMMENT '商品状态'")
    private int status;
    private Date createDate;
    private Date updateDate;
}
