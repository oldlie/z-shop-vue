package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/backend")
@RestController
public class AdminTagController {

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
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
}
