package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.component.CopyObject;
import com.oldlie.zshop.zshopvue.model.constant.ResponseCode;
import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityProfileRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommodityProfileService {

    private CommodityProfileRepository commodityProfileRepository;

    @Autowired
    public void setCommodityProfileRepository(CommodityProfileRepository commodityProfileRepository) {
        this.commodityProfileRepository = commodityProfileRepository;
    }

    public SimpleResponse<Long> store(final CommodityProfile profile) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityProfile target = null;
        if (profile.getId() > 0) {
            Optional<CommodityProfile> optional = this.commodityProfileRepository
                    .findOne(
                            (root, query, cb)-> cb.equal(root.get("id"), profile.getId())
                    );
            if (optional.isPresent()) {
                target = optional.get();
            } else {
                response.setStatus(ResponseCode.FAILED);
                response.setMessage("Commodity does not exist.");
                return response;
            }
        } else {
            target = new CommodityProfile();
        }
        CopyObject<CommodityProfile> copyObject = new CopyObject<>();
        target = copyObject.copyValue2Entity(profile, target);

        if (target == null) {
            response.setStatus(ResponseCode.FAILED);
            response.setMessage("CopyValue Failed.");
            return response;
        }

        target = this.commodityProfileRepository.save(target);
        response.setItem(target.getId());

        return response;
    }

    public SimpleResponse<CommodityProfile> commodityProfile(final Long commodityId) {
        SimpleResponse<CommodityProfile> response = new SimpleResponse<>();
        Optional<CommodityProfile> optional = this.commodityProfileRepository.findOne(
                (root, query, cb)->cb.equal(root.get("commodityId"), commodityId)
        );
        if (optional.isPresent()) {
            response.setItem(optional.get());
        } else {
            response.setStatus(ResponseCode.FAILED);
            response.setMessage("Commodity profile does not exist.");
        }
        return response;
    }

    @Transactional
    public BaseResponse delete(final Long id) {
        BaseResponse response = new BaseResponse();
        this.commodityProfileRepository.findOne(
                (r, q, cb)->cb.equal(r.get("id"), id)
        ).ifPresent(x-> this.commodityProfileRepository.delete(x));
        return response;
    }

    @Transactional
    public BaseResponse deleteByCommodityId(final Long commodityId) {
        List<CommodityProfile> list = this.commodityProfileRepository.findAll(
                (r, q, cb)->cb.equal(r.get("commodityId"), commodityId)
        );
        this.commodityProfileRepository.deleteAll(list);
        return new BaseResponse();
    }

}
