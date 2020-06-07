package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommodityFormulaRepository extends JpaRepository<CommodityFormula, Long>,
        JpaSpecificationExecutor<CommodityFormula> {

    void deleteAllByCommodityId(Long cid);

    List<CommodityFormula> findAllByCommodityIdOrderByIdAsc(Long cid);

    /**
     * Find commodity formula by commodity id
     * @param commodityId commodity id
     * @return formula
     */
    CommodityFormula findOneByIdAndCommodityId(long id, long commodityId);
}
