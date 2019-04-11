package com.oldlie.zshop.zshopvue.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstByRole(String role);
}
