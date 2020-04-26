package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.CarouselNavigation;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CarouselNavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/backend")
@Validated
public class AdminCarouselNavController {

    private CarouselNavigationService service;

    @Autowired
    public void setService(CarouselNavigationService service) {
        this.service = service;
    }

    @PostMapping("/carousel-nav")
    public SimpleResponse<Long> store(@RequestBody @Valid Tag tag) {
        return this.service.store(tag);
    }

    @DeleteMapping("/carousel-nav/{id}")
    public BaseResponse delete(@PathVariable(name = "id") long id) {
        return this.service.delete(id);
    }

    @GetMapping("/carousel-nav-list")
    public ListResponse<CarouselNavigation> list() {
        return this.service.list();
    }
}
