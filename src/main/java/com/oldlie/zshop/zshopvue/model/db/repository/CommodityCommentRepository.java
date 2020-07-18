package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CommodityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 * @date 2020/7/18
 */
public interface CommodityCommentRepository extends JpaRepository<CommodityComment, Long>,
        JpaSpecificationExecutor<CommodityComment> {
}
