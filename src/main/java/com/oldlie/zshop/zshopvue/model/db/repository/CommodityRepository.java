package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommodityRepository extends JpaRepository<Commodity, Long>, JpaSpecificationExecutor<Commodity> {

    Commodity findOneById(long id);

    @Query(value = "FROM Commodity as c join CommodityTag as t on c.id=t.commodityId " +
            "where t.tagId=:tagId and c.status=:status")
    Page<Commodity> findAllByTagId(@Param("tagId") long tagId, @Param("status") int commodityStatus, Pageable pageable);
}
