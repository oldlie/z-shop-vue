package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRespostiory extends JpaRepository<Article, Long> {
}
