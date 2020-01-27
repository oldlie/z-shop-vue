package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.db.ArticleTag;
import com.oldlie.zshop.zshopvue.model.db.repository.ArticleRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.ArticleTagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ArticleService {

    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    private ArticleTagRepository articleTagRepository;

    @Autowired
    public void setArticleTagRepository(ArticleTagRepository articleTagRepository) {
        this.articleTagRepository = articleTagRepository;
    }

    @Transactional
    public SimpleResponse<Long> store(AppRequest<Article> request, Long uid, String username) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Article article = request.getBody();
        Article target = null;
        if (article.getId() > 0) {
            target = this.articleRepository.findById(article.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("你要编辑的文章已经不存在了。");
                return response;
            }
        } else {
            target = new Article();
        }
        target.setAgreeCount(article.getAgreeCount());
        target.setAllowComment(article.getAllowComment());
        target.setAuthor(article.getAuthor());
        target.setContent(article.getContent());
        target.setImageUrl(article.getImageUrl());
        String publisher = null;
        if (uid <= 0) {
            target.setPublisher(username);
            target.setPublisherId(uid);
        } else {
            target.setPublisher(article.getPublisher());
            target.setPublisherId(article.getPublisherId());
        }
        target.setRankCount(article.getRankCount());
        target.setRanking(article.getRanking());
        target.setShareCount(article.getShareCount());
        target.setStatus(article.getStatus());
        target.setSummary(article.getSummary());
        target.setTitle(article.getTitle());
        target.setViewCount(article.getViewCount());
        return response;
    }

    public SimpleResponse<Long> storeArticleTag(Long articleId, Long tagId) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        ArticleTag articleTag = this.articleTagRepository.findFirstByArticleIdAndTagId(articleId, tagId);
        if (articleTag == null) {
            articleTag = new ArticleTag();
        }
        articleTag.setArticleId(articleId);
        articleTag.setTagId(tagId);
        articleTag = this.articleTagRepository.save(articleTag);
        response.setItem(articleTag.getId());
        return response;
    }

    @Transactional
    public BaseResponse delete(Long id) {
        BaseResponse response = new BaseResponse();
        Article article = this.articleRepository.findById(id).orElse(null);
        if (article != null) {
            this.articleTagRepository.deleteAllByArticleId(article.getId());
            this.articleRepository.delete(article);
        }
        return response;
    }


    public PageResponse<Article> list(String key, String value, int index, int size, String orderBy, String order) {
        PageResponse<Article> response = new PageResponse<>();

        Page<Article> page;

        if (StringUtils.isNotEmpty(key)) {
            page = this.articleRepository.findAll(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value),
                    ZsTool.pageable(index, size, orderBy, order)
            );
        } else {
            page = this.articleRepository.findAll(ZsTool.pageable(index, size, orderBy, order));
        }

        response.setList(page.getContent());
        response.setTotal(page.getTotalElements());

        return response;
    }
}
