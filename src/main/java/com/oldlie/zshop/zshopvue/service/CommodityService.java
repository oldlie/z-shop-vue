package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.component.CopyObject;
import com.oldlie.zshop.zshopvue.model.ComRequest;
import com.oldlie.zshop.zshopvue.model.constant.ResponseCode;
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

/**
 * 商品相关操作服务
 *
 * 1.创建商品
 *  a，先创建商品的概要信息，包括名称，缩略图等
 *  b，创建成功之后再创建对应的详细信息，包括规格，详细，展示图片
 *  c，细览创建成功之后创建商品套装，包含套装的价格，库存数
 *  d，套装创建之后决定是否上加，或是预览
 *
 * 2.商品的修改
 *  步骤应该与商品创建一致
 */
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


    // region 商品规格模板
    /**
     * 添加商品规格模板
     *
     * @param request 还有模板对象的请求
     * @return 返回创建、修改成功之后的ID
     */
    public SimpleResponse<Long> storeCommoditySpecificationTemplate(ComRequest<CommoditySpecificationTemplate> request) {
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

    /**
     * 列出商品规格模板供创建商品的时候选择
     *
     * @param page 分页的页码，起始位置是1
     * @param size 分页每页的size
     * @return 返回指定范围的模板
     */
    public PageResponse<CommoditySpecificationTemplate> listCommoditySpecificaitonTemplte(int page, int size) {
        PageResponse<CommoditySpecificationTemplate> response = new PageResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<CommoditySpecificationTemplate> templates = this.commoditySpecificationTemplateRepository.findAll(pageable);
        response.setTotal(templates.getTotalElements());
        response.setList(templates.getContent());
        return response;
    }

    /**
     * 删除商品的模板
     * @param request 要删除的ID
     * @return 删除是否成功
     */
    public BaseResponse deleteCommoditySpecificationTemplate(ComRequest<Long> request) {
        BaseResponse response = new BaseResponse();
        Long id = request.getBody();
        this.commoditySpecificationTemplateRepository.deleteById(id);
        return response;
    }
    // endregion

    // region 商品

    public SimpleResponse<Long> storeCommodity(ComRequest<Commodity> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Commodity commodity = request.getBody();
        Commodity target;
        if (commodity.getId() > 0) {
            target = this.commodityRepository.findById(commodity.getId()).orElse(null);
            if (target == null) {
                response.setStatus(ResponseCode.FAILED);
                response.setMessage("Commodity is null");
                return response;
            }
        } else {
            target = new Commodity();
        }
        target.setIntroduction(commodity.getIntroduction());
        target.setLikeCount(commodity.getLikeCount());
        target.setShareCount(commodity.getShareCount());
        target.setStatus(commodity.getStatus());
        target.setThumbnail(commodity.getThumbnail());
        target.setTitle(commodity.getTitle());
        target.setViewCount(commodity.getViewCount());

        target = this.commodityRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public BaseResponse deleteCommodity(ComRequest<Long> request) {
        BaseResponse response = new BaseResponse();
        Long id = request.getBody();
        +
        return response;
    }

    // endregion
}
