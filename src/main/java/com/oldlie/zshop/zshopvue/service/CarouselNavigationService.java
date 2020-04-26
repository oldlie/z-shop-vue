package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.CarouselNavigation;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.db.repository.CarouselNavigationRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CarouselNavigationService {

    private CarouselNavigationRepository repository;

    @Autowired
    public void setRepository(CarouselNavigationRepository repository) {
        this.repository = repository;
    }

    public SimpleResponse<Long> store(Tag tag) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        long count = this.repository.count();
        if (count >= 9) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("最多设置9个导航");
            return response;
        }
        CarouselNavigation navigation = new CarouselNavigation();
        navigation.setTag(tag);
        navigation = this.repository.save(navigation);
        response.setItem(navigation.getId());
        return response;
    }

    public BaseResponse delete(final long id) {
        BaseResponse response = new BaseResponse();
        this.repository.findOne((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id))
                .ifPresent(x -> this.repository.delete(x));
        return response;
    }

    public ListResponse<CarouselNavigation> list () {
        ListResponse<CarouselNavigation> response = new ListResponse<>();
        List<CarouselNavigation> list = this.repository.findAll();
        response.setList(list);
        return response;
    }
}
