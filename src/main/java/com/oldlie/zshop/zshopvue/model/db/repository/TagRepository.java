package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    List<Tag> findAllByParentIdOrderByIdAsc(Long parentId);
}
