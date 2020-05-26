package com.oldlie.zshop.zshopvue.controller.pubapi;

import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.db.Carousel;
import com.oldlie.zshop.zshopvue.model.db.Commodity;
import com.oldlie.zshop.zshopvue.model.db.QuickNavTag;
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
    public PageResponse<Commodity> commodities(long tagId,
                                             int page,
                                             int size) {
        return this.commodityService.commodities(tagId, page, size, "id", "desc");
    }

    @GetMapping(value = "/commodities/{page}/{size}/{key}/{value}")
    public PageResponse<Commodity> commodities(int page,
                                               int size,
                                               String key,
                                               String value) {
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
    public PageResponse<Article> articles(@RequestParam("tagId") long tagId,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size) {
        return this.articleService.articles(tagId, page, size);
    }
}
