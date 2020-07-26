package com.oldlie.zshop.zshopvue.controller.pubapi;

import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.front.CommoditiesWithTag;
import com.oldlie.zshop.zshopvue.model.front.CommodityInfo;
import com.oldlie.zshop.zshopvue.model.front.HomeArticles;
import com.oldlie.zshop.zshopvue.model.front.TagCommodities;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.ArticleService;
import com.oldlie.zshop.zshopvue.service.CarouselService;
import com.oldlie.zshop.zshopvue.service.CommodityService;
import com.oldlie.zshop.zshopvue.service.QuickNavTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.Map;

/**
 * @author oldlie
 */
@RestController
@RequestMapping("/public/home")
public class HomeRestController {

    @Autowired
    private ArticleService articleService;
    private CarouselService carouselService;
    private CommodityService commodityService;

    @Autowired
    public void setCarouselService(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    private QuickNavTagService quickNavTagService;

    @Autowired
    public void setQuickNavTagService(QuickNavTagService quickNavTagService) {
        this.quickNavTagService = quickNavTagService;
    }

    @GetMapping("/carousels")
    public ListResponse<Carousel> carouselList() {
        return this.carouselService.list();
    }

    @GetMapping("/quick-nav-tags")
    public ListResponse<QuickNavTag> quickNavTagList () {
        return this.quickNavTagService.list();
    }

    @GetMapping("/top-commodities")
    public SimpleResponse<TagCommodities> topProducts () {
        return this.commodityService.topCommodities();
    }

    @GetMapping("/commodities")
    public ListResponse<TagCommodities> tagCommoditiesList() {
        return this.commodityService.homeCommodities();
    }

    @GetMapping("/commodity/{id}")
    public SimpleResponse<CommodityInfo>  commodity(@PathVariable("id") long id) {
        return this.commodityService.commodityInfo(id);
    }

    @GetMapping(value = "/commodities/{tagId}/{page}/{size}")
    public SimpleResponse<CommoditiesWithTag> commodities(@PathVariable("tagId") long tagId,
                                                          @PathVariable("page") int page,
                                                          @PathVariable("size") int size) {
        return this.commodityService.commodities(tagId, page, size, "id", "desc");
    }

    @GetMapping(value = "/commodities/{page}/{size}/{key}/{value}")
    public SimpleResponse<CommoditiesWithTag> commodities(@PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               @PathVariable("key") String key,
                                               @PathVariable("value") String value) throws Exception {
        value = URLDecoder.decode(value, "utf-8");
        return this.commodityService.commodities(key, value, page, size, "id", "desc");
    }

    @GetMapping(value = "/article/{id}")
    public SimpleResponse<Article> article(@PathVariable("id") long id) {
        return this.articleService.article(id);
    }

    @GetMapping("/articles")
    public SimpleResponse<HomeArticles> homeArticles () {
        return this.articleService.homeArticles();
    }

    @GetMapping("/articles/{tagId}/{page}/{size}")
    public PageResponse<Article> articles(@PathVariable("tagId") long tagId,
                                           @PathVariable("page") int page,
                                           @PathVariable("size") int size) {
        return this.articleService.articles(tagId, page, size);
    }

    @GetMapping("/articles/latest")
    public ListResponse<Article> latestArticles() {
        return this.articleService.latestArticles();
    }

    @GetMapping("/commodity/comment/info")
    public SimpleResponse<Map<String, Object>> commentInfo(@RequestParam("cid") long cid) {
        return this.commodityService.commentInfo(cid);
    }

    @GetMapping(value = "/commodity/comments")
    public PageResponse<CommodityComment> comments(@RequestParam("cid") long cid,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size) {
        return this.commodityService.comments(cid, page, size);
    }
}
