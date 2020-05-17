package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.HomeTag;
import com.oldlie.zshop.zshopvue.model.db.repository.HomeCommodityTagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HomeCommodityTagService {
    private HomeCommodityTagRepository repository;

    @Autowired
    public void setRepository(HomeCommodityTagRepository repository) {
        this.repository = repository;
    }

    public SimpleResponse<HomeTag> store(HomeTag source) {
        SimpleResponse<HomeTag> response = new SimpleResponse<>();
        HomeTag target = null;
        if (source.getId() != null && source.getId() > 0) {
            Optional<HomeTag> optional = this.repository.findById(source.getTagId());
            if (optional.isPresent()) {
                target = optional.get();
            } else {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("要修改的标签已经不存在了");
                return response;
            }
        } else {
            target = new HomeTag();
        }
        ObjectCopy<HomeTag> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(source, target);
        target = this.repository.save(target);
        response.setItem(target);
        return response;
    }

    public BaseResponse delete(long tagId) {
        this.repository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tagId"), tagId)
        ).ifPresent(
                x -> this.repository.delete(x)
        );
        return new BaseResponse();
    }

    public ListResponse<HomeTag> list() {
        ListResponse<HomeTag> response = new ListResponse<>();
        List<HomeTag> list = this.repository.findAll();
        response.setList(list);
        return response;
    }
}
