package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oldlie
 * @date 2020/11/1
 */
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_role_permission")
@ToString
public class RolePermission extends BaseEo {

    public final static String RID = "rid";
    public final static String PID = "pid";
    public final static String ROLE = "role";

    private long rid;
    private long pid;
    private String role;
}
