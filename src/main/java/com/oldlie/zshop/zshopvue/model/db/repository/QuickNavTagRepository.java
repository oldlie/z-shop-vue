package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.QuickNavTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuickNavTagRepository extends JpaRepository<QuickNavTag, Long>, JpaSpecificationExecutor<QuickNavTag> {
}
