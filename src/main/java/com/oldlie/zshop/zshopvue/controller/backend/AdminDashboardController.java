package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.db.Carousel;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CarouselService;
import com.oldlie.zshop.zshopvue.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("/admin/dashboard")
@Slf4j
public class AdminDashboardController {

    private CarouselService carouselService;

    @Autowired
    public void setCarouselService(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    // region Carousel
    @GetMapping(value = "/carousel")
    public ListResponse<Carousel> listCarousel() {
        return this.carouselService.list();
    }

    @PostMapping(value = "/carousel", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SimpleResponse<Long> storeCarousel(@RequestBody AppRequest<Carousel> request) {
        return this.carouselService.store(request);
    }

    @DeleteMapping(value = "/carousel/{id}")
    public BaseResponse deleteCarousel(@PathVariable("id") Long id) {
        return this.carouselService.delete(id);
    }
    // endregion

    // region Menu Tag
    @PostMapping(value = "/tag", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public SimpleResponse<Long> storeTag(AppRequest<Tag> request) {
        return this.tagService.store(request);
    }

    @DeleteMapping(value = "/tag/{id}")
    public BaseResponse deleteTag(@PathVariable("id") Long id) {
        return this.tagService.delete(id);
    }

    @GetMapping(value = "/tags/{id}")
    public ListResponse<Tag> listTag(@PathVariable("id") Long parentId) {
        return this.tagService.list(parentId);
    }
    // endregion
}
