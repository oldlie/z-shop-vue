package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class AdminArticleController {

    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/articles/{index}-{size}-{orderBy}-{order}")
    public PageResponse<Article> page(@PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("orderBy") String orderBy,
                                      @PathVariable("order") String order) {
       return this.articleService.list(null, null, index, size, orderBy, order);
    }

    @GetMapping(value = "/articles/{key}/{value}/{index}-{size}-{orderBy}-{order}")
    public PageResponse<Article> page(@PathVariable("key") String key,
                                      @PathVariable("value") String value,
                                      @PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("orderBy") String orderBy,
                                      @PathVariable("order") String order){
        return this.articleService.list(key, value, index, size, orderBy, order);
    }

    @GetMapping(value = "/article/{id}")
    public SimpleResponse<Article> article(@PathVariable("id") long id) {
         return this.articleService.article(id);
    }

    /**
    @PostMapping(value = "/article", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleResponse<Long> store(@RequestBody AppRequest<Article> appRequest,
                                      @SessionAttribute("uid") long uid,
                                      @SessionAttribute("username") String username) {
        return this.articleService.store(appRequest, uid, username);
    }
     */

    @PostMapping(value = "/article", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public SimpleResponse<Long> store(@RequestParam("id") long id,
                                      @RequestParam("title") String title,
                                      @RequestParam("author") String author,
                                      @RequestParam("summary") String summary,
                                      @RequestParam("content") String content,
                                      @RequestParam("thumbnail") String thumbnail,
                                      @RequestParam("allowComment") int allowComment,
                                      @SessionAttribute("uid") long uid,
                                      @SessionAttribute("username") String username) {
       Article article = new Article();
       article.setId(id);
       article.setTitle(title);
       article.setAuthor(author);
       article.setSummary(summary);
       article.setContent(content);
       article.setImageUrl(thumbnail);
       article.setAllowComment(allowComment);
       article.setPublisherId(uid);
       article.setPublisher(username);
       return this.articleService.store(article);
    }

    @DeleteMapping(value = "/article/{id}")
    public BaseResponse delete(@PathVariable("id") long id) {
       return this.articleService.delete(id);
    }
}
