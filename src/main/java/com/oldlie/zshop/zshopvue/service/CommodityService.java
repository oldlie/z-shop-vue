package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.ZshopReqeust;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommodityService {

    private CommodityRepository commodityRepository;
    private CommodityFormulaRepository commodityFormulaRepository;
    private CommodityProfileRepository commodityProfileRepository;
    private CommoditySpecificationTemplateRepository commoditySpecificationTemplateRepository;

    @Autowired
    public void setCommodityRepository(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }
    @Autowired
    public void setCommodityFormulaRepository(CommodityFormulaRepository commodityFormulaRepository) {
        this.commodityFormulaRepository = commodityFormulaRepository;
    }
    @Autowired
    public void setCommodityProfileRepository(CommodityProfileRepository commodityProfileRepository) {
        this.commodityProfileRepository = commodityProfileRepository;
    }
    @Autowired
    public void setCommoditySpecificationTemplateRepository(
            CommoditySpecificationTemplateRepository commoditySpecificationTemplateRepository) {
        this.commoditySpecificationTemplateRepository = commoditySpecificationTemplateRepository;
    }

    public SimpleResponse<Long> storeCommoditySpecificationTemplate(ZshopReqeust<CommoditySpecificationTemplate> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommoditySpecificationTemplate template = request.getBody();
        CommoditySpecificationTemplate target;
        if (template.getId() > 0) {
            target = this.commoditySpecificationTemplateRepository.findById(template.getId())
                    .orElse(new CommoditySpecificationTemplate());
        } else {
            target = new CommoditySpecificationTemplate();
        }
        target.setTitle(template.getTitle());
        target.setTemplate(template.getTemplate());
        return response;
    }

    public PageResponse<CommoditySpecificationTemplate> listCommoditySpecificaitonTemplte(int page, int size) {
        PageResponse<CommoditySpecificationTemplate> response = new PageResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<CommoditySpecificationTemplate> templates = this.commoditySpecificationTemplateRepository.findAll(pageable);
        response.setTotal(templates.getTotalElements());
        response.setList(templates.getContent());
        return response;
    }

    public BaseResponse deleteCommoditySpecificationTemplate(ZshopReqeust<Long> reqeust) {
        BaseResponse response = new BaseResponse();
        Long id = reqeust.getBody();
        return response;
    }
}
