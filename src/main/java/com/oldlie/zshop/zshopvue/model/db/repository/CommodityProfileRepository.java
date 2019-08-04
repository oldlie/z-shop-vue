package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommodityProfileRepository extends JpaRepository<CommodityProfile, Long>,
        JpaSpecificationExecutor<CommodityProfile> {

    void deleteByCommodityId(Long commodityId);
}
