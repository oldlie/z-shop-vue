package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommodityTagRepository extends JpaRepository<CommodityTag, Long> {
    Long countByTagId(Long id);
    CommodityTag findFirstByCommodityIdAndTagId(Long commodityId, Long tagId);
    void deleteAllByCommodityId(Long commodityId);
    void deleteByCommodityIdAndTagId(Long commodityId, Long tagId);
}
