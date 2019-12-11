package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.CommodityFormula;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityFormulaRepository;
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
public class CommodityFormulaService {

    private CommodityFormulaRepository commodityFormulaRepository;

    @Autowired
    public void setCommodityRepository(CommodityFormulaRepository commodityRepository) {
        this.commodityFormulaRepository = commodityRepository;
    }

    public SimpleResponse<Long> store(final CommodityFormula formula) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityFormula target = null;
        if (formula.getId() > 0) {
            Optional<CommodityFormula> optional = this.commodityFormulaRepository.findOne(
                    (r, q, cb)->cb.equal(r.get("id"), formula.getId())
            );
            if (optional.isPresent()) {
                target = optional.get();
            } else {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("Commodity formula does not exist");
                return response;
            }
        } else {
            target = new CommodityFormula();
        }

        ObjectCopy<CommodityFormula> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(formula, target);
        if (target == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("Copy value to target error.");
            return response;
        }
        target = this.commodityFormulaRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public ListResponse<CommodityFormula> commodityFormulaList(final Long commodityId) {
        ListResponse<CommodityFormula> response = new ListResponse<>();
        List<CommodityFormula> list = this.commodityFormulaRepository.findAll(
                (r, q, cb)->cb.equal(r.get("commodityId"), commodityId)
        );
        response.setList(list);
        return response;
    }

    @Transactional
    public BaseResponse delete(final Long id) {
        BaseResponse response = new BaseResponse();
        this.commodityFormulaRepository.findOne(
                (r, q, cb)->cb.equal(r.get("id"), id)
        ).ifPresent(x->this.commodityFormulaRepository.delete(x));
        return response;
    }
}
