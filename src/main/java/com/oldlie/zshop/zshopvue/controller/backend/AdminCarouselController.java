package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.Carousel;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CarouselService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/backend")
@Validated
public class AdminCarouselController {

    private CarouselService carouselService;

    @Autowired
    public void setCarouselService(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @GetMapping("/carousels")
    public ListResponse<Carousel> list() {
        return this.carouselService.list();
    }

    @PostMapping("/carousel")
    public SimpleResponse<Long> store(@RequestBody @Valid Carousel carousel) {
        return this.carouselService.store(carousel);
    }

    @DeleteMapping("/carousel/{id}")
    public BaseResponse delete(@PathVariable(name = "id") long id) {
        return this.carouselService.delete(id);
    }

}
