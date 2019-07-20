package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "t_article")
@ToString
@NoArgsConstructor
public class Article extends BaseEO {
    private String title;
    private String summary;
    private String imageUrl;
    private String author;
    private Long publisherId;
    private String publisher;
    @Column(columnDefinition = "text")
    private String content;
    private int status;
    private long viewCount;
    private long agreeCount;
    private long shareCount;
    private int allowComment;
    @Column(columnDefinition = "int default 10 comment '平均分，默认10分满分'")
    private int ranking;
    private long rankCount;
}
