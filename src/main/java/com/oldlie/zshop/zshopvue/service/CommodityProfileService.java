package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityProfileRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
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
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("Commodity does not exist.");
                return response;
            }
        } else {
            target = new CommodityProfile();
        }

        ObjectCopy<CommodityProfile> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(profile, target);

        if (target == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("CopyValue Failed.");
            return response;
        }

        target = this.commodityProfileRepository.save(target);
        response.setItem(target.getId());

        return response;
    }

    public BaseResponse updateSpecification(final long commodityId, final String specification) {
        BaseResponse response = new BaseResponse();
        CommodityProfile target = this.initCommodityProfile(commodityId);
        target.setSpecification(specification);
        this.commodityProfileRepository.save(target);
        return response;
    }

    public BaseResponse updateImages(final long commodityId, final String images) {
        BaseResponse response = new BaseResponse();
        CommodityProfile target = this.initCommodityProfile(commodityId);
        target.setImages(images);
        this.commodityProfileRepository.save(target);
        return response;
    }

    public BaseResponse updateDetails(final long commodityId, final String detail) {
        BaseResponse response = new BaseResponse();
        CommodityProfile target = this.initCommodityProfile(commodityId);
        target.setDetail(detail);
        this.commodityProfileRepository.save(target);
        return response;
    }

    private CommodityProfile initCommodityProfile(final long commodityId) {
        CommodityProfile target;
        Optional<CommodityProfile> optional = this.commodityProfileRepository
                .findOne(
                        (root, query, cb) -> cb.equal(root.get("commodityId"), commodityId)
                );
        if (!optional.isPresent()) {
            target = new CommodityProfile();
            target.setCommodityId(commodityId);
        } else {
            target = optional.get();
        }
        return target;
    }

    public SimpleResponse<CommodityProfile> commodityProfile(final Long commodityId) {
        SimpleResponse<CommodityProfile> response = new SimpleResponse<>();
        Optional<CommodityProfile> optional = this.commodityProfileRepository.findOne(
                (root, query, cb)->cb.equal(root.get("commodityId"), commodityId)
        );
        if (optional.isPresent()) {
            response.setItem(optional.get());
        } else {
            response.setStatus(HTTP_CODE.FAILED);
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
