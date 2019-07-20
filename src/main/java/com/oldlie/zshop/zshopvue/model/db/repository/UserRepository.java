package com.oldlie.zshop.zshopvue.model.db.repository;

import java.util.List;

import com.oldlie.zshop.zshopvue.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // List<User> findByUsername(String username);
    User findFirstByUsername(String username);
}
