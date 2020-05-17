package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.HomeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HomeCommodityTagRepository extends JpaRepository<HomeTag, Long>,
        JpaSpecificationExecutor<HomeTag> {
}
