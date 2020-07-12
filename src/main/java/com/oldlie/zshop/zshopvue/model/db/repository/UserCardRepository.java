package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 * @date 2020/7/12
 */
public interface UserCardRepository extends JpaRepository<UserCard, Long>, JpaSpecificationExecutor<UserCard> {
}
