package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommodityTagRepository extends JpaRepository<CommodityTag, Long>,
        JpaSpecificationExecutor<CommodityTag> {
}
