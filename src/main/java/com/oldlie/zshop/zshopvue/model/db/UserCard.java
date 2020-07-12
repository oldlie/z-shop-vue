package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oldlie
 * @date 2020/7/12
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_user_card")
@ToString
public class UserCard extends BaseEO{
    private long uid;
    private long cardId;
}
