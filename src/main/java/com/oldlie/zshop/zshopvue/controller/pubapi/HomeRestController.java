package com.oldlie.zshop.zshopvue.controller.pubapi;

import com.oldlie.zshop.zshopvue.model.db.Carousel;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/home")
public class HomeRestController {

    private CarouselService carouselService;

    @Autowired
    public void setCarouselService(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @GetMapping("/carousels")
    public ListResponse<Carousel> carouselList() {
        return this.carouselService.list();
    }
}
