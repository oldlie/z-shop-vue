package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommodityRepository extends JpaRepository<Commodity, Long> {
}
