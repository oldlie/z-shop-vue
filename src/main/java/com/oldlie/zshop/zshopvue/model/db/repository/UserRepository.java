package com.oldlie.zshop.zshopvue.model.db.repository;

import java.util.List;

import com.oldlie.zshop.zshopvue.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // List<FrontUser> findByUsername(String username);
    User findFirstByUsername(String username);
}
