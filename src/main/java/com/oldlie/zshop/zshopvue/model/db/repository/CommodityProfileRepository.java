package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommodityProfileRepository extends JpaRepository<CommodityProfile, Long> {

    void deleteByCommodityProfileByCommodityId(Long cid);

    CommodityProfile findFirstByCommodityId(Long cid);
}
