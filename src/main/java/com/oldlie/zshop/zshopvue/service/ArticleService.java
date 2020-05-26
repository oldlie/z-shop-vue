package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.db.ArticleTag;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.db.repository.ArticleRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.ArticleTagRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.TagRepository;
import com.oldlie.zshop.zshopvue.model.front.HomeArticles;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    private TagRepository tagRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(rollbackFor = Exception.class)
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

    public SimpleResponse<Long> store(Article article) {
       SimpleResponse<Long> response = new SimpleResponse<>();
       Article target;
       if (article.getId() > 0) {
           Optional<Article> optional = this.articleRepository.findOne(
                   (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), article.getId())
           );
           if (!optional.isPresent()) {
               response.setStatus(HTTP_CODE.FAILED);
               response.setMessage("您要编辑的文章已经不存在了。");
               return response;
           }
           target = optional.get();
       }  else {
           target = new Article();
       }
       ObjectCopy<Article> copy = new ObjectCopy<>();
       target = copy.copy(article, target);
       target = this.articleRepository.save(target);
       response.setItem(target.getId());
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

    public SimpleResponse<Article> article(final long id) {
        SimpleResponse<Article> response = new SimpleResponse<>();
        Optional<Article> optional = this.articleRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id)
        );
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("没有找到请求的文章");
        } else {
            Article article = optional.get();
            response.setItem(article);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public ListResponse<Tag> tags(final long id) {
        ListResponse<Tag> response = new ListResponse<>();
        List<ArticleTag> articleTags = this.articleTagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("articleId"), id)
        );
        if (articleTags.size() <= 0) {
            return response;
        }
        List<Tag> tags =this.tagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("id"));
                    for (ArticleTag articleTag : articleTags) {
                        in.value(articleTag.getTagId());
                    }
                    return in;
                }
        );
        response.setList(tags);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<Long> tag(final long articleId, final long tagId) {
       SimpleResponse<Long> response = new SimpleResponse<>();
       Optional<ArticleTag> optional = this.articleTagRepository.findOne(
               (root, criteriaQuery, criteriaBuilder) -> {
                   Predicate predicate1 = criteriaBuilder.equal(root.get("articleId"), articleId);
                   Predicate predicate2 = criteriaBuilder.equal(root.get("tagId"), tagId);
                   return criteriaBuilder.and(predicate1, predicate2);
               }
       );
       if (optional.isPresent()) {
           response.setStatus(HTTP_CODE.FAILED);
           response.setMessage("这个标签已经添加了");
           return response;
       }
       ArticleTag articleTag = new ArticleTag();
       articleTag.setArticleId(articleId);
       articleTag.setTagId(tagId);
       articleTag = this.articleTagRepository.save(articleTag);
       response.setItem(articleTag.getId());
       return response;
    }

    public BaseResponse removeTag(final long articleId, final long tagId) {
        BaseResponse response = new BaseResponse();
        this.articleTagRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicateArticleId = criteriaBuilder.equal(root.get("articleId"), articleId);
                    Predicate predicateTagId = criteriaBuilder.equal(root.get("tagId"), tagId);
                    return criteriaBuilder.and(predicateArticleId, predicateTagId);
                }
        ).ifPresent(x -> this.articleTagRepository.delete(x));
        return response;
    }

    public PageResponse<Article> articles(long tagId, int pageIndex, int size) {
        Page page = this.articleRepository.findAllByTagId(tagId,
                ZsTool.pageable(pageIndex, size, "id", "desc"));
        List content = page.getContent();
        List<Article> articles = new LinkedList<>();
        content.forEach(x -> {
            Object[] object = (Object[]) x;
            articles.add((Article) object[0]);
        });

        PageResponse<Article> response = new PageResponse<>();
        response.setList(articles);
        response.setTotal(page.getTotalElements());

        return response;
    }

    /**
     * 一次性获取首页的文章tag，以及
     * @return
     */
    public SimpleResponse<HomeArticles> homeArticles () {
        SimpleResponse<HomeArticles> response = new SimpleResponse<>();
        List<Tag> tags = this.tagRepository.findAllByHomeTag(1);
        Tag tag = tags.get(0);

        PageResponse<Article> article = this.articles(tag.getId(), 1, 10);

        response.setItem(HomeArticles.builder()
                .firstTag(tag)
                .tags(tags)
                .articles(article.getList())
                .total(article.getTotal())
                .build());

        return response;
    }
}
