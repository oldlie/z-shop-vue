package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Carousel;
import com.oldlie.zshop.zshopvue.model.db.repository.CarouselRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarouselService {

    private CarouselRepository carouselRepository;

    @Autowired
    public void setCarouselRepository(CarouselRepository carouselRepository) {
        this.carouselRepository = carouselRepository;
    }

    public SimpleResponse<Long> store(Carousel carousel) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Carousel target = null;
        if (carousel.getId() != null && carousel.getId() > 0) {
            target = this.carouselRepository.findById(carousel.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("找不到要修改的轮播图了。");
                return response;
            }
        } else {
            target = new Carousel();
        }
        target.setImageUrl(carousel.getImageUrl());
        target.setItemOrder(carousel.getItemOrder());
        target.setSummary(carousel.getSummary());
        target.setTitle(carousel.getTitle());
        target.setUrl(carousel.getUrl());
        target = this.carouselRepository.save(target);

        response.setItem(target.getId());
        return response;
    }

    public BaseResponse delete(Long id) {
        BaseResponse response = new BaseResponse();
        Carousel carousel = this.carouselRepository.findById(id).orElse(null);
        if (carousel != null) {
            this.carouselRepository.delete(carousel);
        }
        return response;
    }

    public ListResponse<Carousel> list() {
        ListResponse<Carousel> response = new ListResponse<>();
        List<Carousel> list =this.carouselRepository.findAll();
        response.setList(list);
        return response;
    }
}
