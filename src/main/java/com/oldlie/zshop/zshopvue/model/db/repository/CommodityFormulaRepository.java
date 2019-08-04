package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityFormula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommodityFormulaRepository extends JpaRepository<CommodityFormula, Long> {

    void deleteAllByCommodityId(Long cid);

    List<CommodityFormula> findAllByCommodityIdOrderByIdAsc(Long cid);
}
