package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommoditySpecificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommoditySpecificationTemplateRepository extends JpaRepository<CommoditySpecificationTemplate, Long>,
        JpaSpecificationExecutor<CommoditySpecificationTemplate> {

}
