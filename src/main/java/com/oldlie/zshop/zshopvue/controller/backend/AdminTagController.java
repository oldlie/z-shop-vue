package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.db.HomeTag;
import com.oldlie.zshop.zshopvue.model.db.QuickNavTag;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.HomeCommodityTagService;
import com.oldlie.zshop.zshopvue.service.QuickNavTagService;
import com.oldlie.zshop.zshopvue.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RequestMapping("/backend")
@RestController
public class AdminTagController {

    private HomeCommodityTagService homeCommodityTagService;
    private TagService tagService;

    @Autowired
    public void setHomeCommodityTagService(HomeCommodityTagService homeCommodityTagService) {
        this.homeCommodityTagService = homeCommodityTagService;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    private QuickNavTagService quickNavTagService;

    @Autowired
    public void setQuickNavTagService(QuickNavTagService quickNavTagService) {
        this.quickNavTagService = quickNavTagService;
    }

    @PostMapping(value = "/tag", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public SimpleResponse<Long> store(@RequestBody AppRequest<Tag> request) {
        return tagService.store(request);
    }

    @DeleteMapping(value = "/tag/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        return tagService.delete(id);
    }

    @GetMapping(value = "/tags/{parentId}/{page}/{size}/{orderBy}/{order}")
    public PageResponse<Tag> tags(@PathVariable("parentId") Long parentId,
                                  @PathVariable("page") int page,
                                  @PathVariable("size") int size,
                                  @PathVariable("orderBy") String orderBy,
                                  @PathVariable("order") String order) {
        return tagService.tags(parentId, page, size, orderBy, order);
    }

    @GetMapping(value = "/tags/{parentId}")
    public ListResponse<Tag> tags(@PathVariable(value = "parentId", required = false) Long id) {
        return this.tagService.list(id == null ? 0L : id);
    }

    @PostMapping(value = "/quick-nav-tags", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse quickNavTags(@RequestBody QuickNavTag[] list) {
        return this.quickNavTagService.update(Arrays.asList(list));
    }

    @PostMapping(value = "/quick-nav-tag", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse quickNavTag(@RequestBody QuickNavTag tag,
                                    HttpServletRequest request) {

        return this.quickNavTagService.store(tag);
    }

    @DeleteMapping(value = "/quick-nav-tag/{id}")
    public BaseResponse deleteQuickNavTag(@PathVariable("id") long id) {
        return this.quickNavTagService.delete(id);
    }

    @GetMapping(value = "/quick-nav-tags")
    public ListResponse<QuickNavTag> listQuickNavTags() {
        return this.quickNavTagService.list();
    }

    @GetMapping(value = "/home-commodity-tags")
    public ListResponse<Tag> homeCommodityTags() {
        return this.tagService.homeCommodityTags();
    }

    @GetMapping(value = "/home-article-tags")
    public ListResponse<Tag> homeArticleTags () {
        return this.tagService.homeArticleTags();
    }

    @PostMapping(value = "/home-tag", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleResponse<HomeTag> homeCommodityTag(@RequestBody HomeTag tag) {
        return this.homeCommodityTagService.store(tag);
    }

    @DeleteMapping(value = "/home-tag/{tagId}")
    public BaseResponse homeCommodityTag(@PathVariable("tagId") long tagId) {
        return this.homeCommodityTagService.delete(tagId);
    }
}
