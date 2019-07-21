package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    Long countByTagId(Long id);
}
