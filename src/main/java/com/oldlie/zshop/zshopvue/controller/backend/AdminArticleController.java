package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/article", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleResponse<Long> store(@RequestBody AppRequest<Article> appRequest,
                                      @SessionAttribute("uid") long uid,
                                      @SessionAttribute("username") String username) {
        return this.articleService.store(appRequest, uid, username);
    }

    @DeleteMapping(value = "/article/{id}")
    public BaseResponse delete(@PathVariable("id") long id) {
       return this.articleService.delete(id);
    }
}
