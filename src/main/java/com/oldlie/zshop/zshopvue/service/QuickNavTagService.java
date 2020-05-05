package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.QuickNavTag;
import com.oldlie.zshop.zshopvue.model.db.repository.QuickNavTagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuickNavTagService {

    private QuickNavTagRepository repository;

    @Autowired
    public void setRepository(QuickNavTagRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BaseResponse update(List<QuickNavTag> list) {
        BaseResponse response = new BaseResponse();
        this.repository.deleteAll();
        this.repository.saveAll(list);
        return response;
    }

    public BaseResponse store(QuickNavTag source) {
        BaseResponse response = new BaseResponse();
        QuickNavTag tag = null;
        if (source.getId() != null && source.getId() > 0) {
            Optional<QuickNavTag> optional = this.repository.findOne(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), source.getId())
            );
            if (optional.isPresent()) {
                tag = optional.get();
            } else {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("要修改的标签不存在了。");
                return response;
            }
        } else {
            tag = new QuickNavTag();
        }
        ObjectCopy<QuickNavTag> copy = new ObjectCopy<>();
        tag = copy.copyValue2Entity(source, tag);
        this.repository.save(tag);
        return response;
    }

    public BaseResponse delete(long id) {
        BaseResponse response = new BaseResponse();
        this.repository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id)
        ).ifPresent(
                x -> this.repository.delete(x)
        );
        return response;
    }

    public ListResponse<QuickNavTag> list() {
        ListResponse<QuickNavTag> response = new ListResponse<>();
        List<QuickNavTag> list = this.repository.findAll();
        response.setList(list);
        return response;
    }
}
