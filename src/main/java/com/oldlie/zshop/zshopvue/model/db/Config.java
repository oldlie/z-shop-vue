package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author oldlie
 * @date 2020/11/1
 */
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_config", uniqueConstraints = @UniqueConstraint(columnNames = { "c_key" }))
@ToString
public class Config extends BaseEo {
    @Column(name = "c_key")
    private String key;
    @Column(name = "c_value")
    private String value;
    @Column(name = "c_title")
    private String title;
    @Column(name = "c_comment")
    private String comment;
    @Column(name = "c_gid")
    private long gid;
    @Column(name = "c_group")
    private String group;
}
