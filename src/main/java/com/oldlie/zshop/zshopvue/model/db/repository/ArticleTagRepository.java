package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long>,
        JpaSpecificationExecutor<ArticleTag> {
    Long countByTagId(Long id);
    ArticleTag findFirstByArticleIdAndTagId(Long articleId, Long tagId);
    void deleteAllByArticleId(Long articleId);
    void deleteByArticleIdAndTagId(Long articleId, Long tagId);
}
