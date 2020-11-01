package com.oldlie.zshop.zshopvue.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.repository.NoRepositoryBean;

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
@NoRepositoryBean
@Table(name = "t_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "c_key" })
})
public class Permission extends BaseEo {
    @Column(name = "c_pid")
    private long parentId;
    @Column(name = "c_key")
    private String key;
    @Column(name = "c_type", columnDefinition = "tinyint default 0 comment '0, URL;1, UI'")
    private int type;
    @Column(name = "c_url")
    private String url;
    @Column(name = "t_title")
    private String title;
    @Column(name = "t_comment")
    private String comment;
}
