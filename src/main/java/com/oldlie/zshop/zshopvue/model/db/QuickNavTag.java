package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_quick_nav_tag")
@ToString
public class QuickNavTag extends BaseEO implements Serializable {
    private long tagId;
    private String tagTitle;
    private int sequence;
}
