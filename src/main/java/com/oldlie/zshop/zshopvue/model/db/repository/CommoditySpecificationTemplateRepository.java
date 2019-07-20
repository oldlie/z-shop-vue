package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommoditySpecificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommoditySpecificationTemplateRepository extends JpaRepository<CommoditySpecificationTemplate, Long> {

    void deleteByCommodityId(Long id);
}
