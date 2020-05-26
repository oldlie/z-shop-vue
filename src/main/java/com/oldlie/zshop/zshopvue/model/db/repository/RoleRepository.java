package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author oldlie
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find first role by role name
     * @param role role name
     * @return role
     */
    Role findFirstByName(String role);
}
