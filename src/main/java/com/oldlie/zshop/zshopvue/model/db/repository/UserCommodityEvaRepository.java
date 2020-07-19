package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.UserCommodityEva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserCommodityEvaRepository extends JpaRepository<UserCommodityEva, Long>,
        JpaSpecificationExecutor<UserCommodityEva> {
}
