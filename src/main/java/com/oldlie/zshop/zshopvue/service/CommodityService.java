package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

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
    private CommodityTagRepository commodityTagRepository;

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
    @Autowired
    public void setCommodityTagRepository(CommodityTagRepository commodityTagRepository) {
        this.commodityTagRepository = commodityTagRepository;
    }
    // region 商品规格模板
    /**
     * 添加商品规格模板
     *
     * @param request 还有模板对象的请求
     * @return 返回创建、修改成功之后的ID
     */
    public SimpleResponse<Long> storeCommoditySpecificationTemplate(AppRequest<CommoditySpecificationTemplate> request) {
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
        target = this.commoditySpecificationTemplateRepository.save(target);
        response.setItem(target.getId());
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
    public BaseResponse deleteCommoditySpecificationTemplate(AppRequest<Long> request) {
        BaseResponse response = new BaseResponse();
        Long id = request.getBody();
        this.commoditySpecificationTemplateRepository.deleteById(id);
        return response;
    }
    // endregion

    // region 商品

    public SimpleResponse<Long> storeCommodity(final Commodity commodity) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Commodity target;
        if (commodity.getId() != null && commodity.getId() > 0) {
            target = this.commodityRepository.findById(commodity.getId()).orElse(null);
            if (target == null) {
                response.setStatus(HTTP_CODE.FAILED);
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

    @Transactional
    public BaseResponse deleteCommodity(final long id) {
        BaseResponse response = new BaseResponse();

        try {
//            this.commodityTagRepository.deleteAllByCommodityId(id);
            List<CommodityTag> list = this.commodityTagRepository.findAll(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("commodityId"), id));
            this.commodityTagRepository.deleteAll(list);

            this.commodityFormulaRepository.deleteAllByCommodityId(id);
            this.commodityRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(1);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public SimpleResponse<Commodity> commodity(Long id) {
        SimpleResponse<Commodity> response = new SimpleResponse<>();
        Commodity commodity = this.commodityRepository.findById(id).orElse(new Commodity());
        response.setItem(commodity);
        return response;
    }

    public PageResponse<Commodity> commodities(int page, int size, String orderBy, String direct) {
        PageResponse<Commodity> response = new PageResponse<>();
        Page<Commodity> commodities = this.commodityRepository.findAll(ZsTool.pageable(page, size, orderBy, direct));
        response.setList(commodities.getContent());
        response.setTotal(commodities.getTotalElements());
        return response;
    }

    public PageResponse<Commodity> commodities(final String field, Object value, int page, int size, String orderBy, String direct) {
        PageResponse<Commodity> response = new PageResponse<>();
        List<CommodityTag> commodityTags = this.commodityTagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(field), "%"+ value + "%"));
        if (commodityTags == null || commodityTags.size() <= 0) {
            return response;
        }

        Page<Commodity> commodities = this.commodityRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
                    commodityTags.forEach(ct -> in.value(ct.getCommodityId()));
                    return in;
                },
                ZsTool.pageable(page, size, orderBy, direct)
        );
        response.setTotal(commodities.getTotalElements());
        response.setList(commodities.getContent());
        return response;
    }

    public SimpleResponse<Long> storeCommodityTag(AppRequest<CommodityTag> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityTag commodityTag = request.getBody();
        CommodityTag target = null;

//        target = this.commodityTagRepository
//                .findFirstByCommodityIdAndTagId(
//                        commodityTag.getCommodityId(),
//                        commodityTag.getTagId());
        Optional<CommodityTag> optional = this.commodityTagRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate commodityId =
                            criteriaBuilder.equal(root.get("commodityId"), commodityTag.getCommodityId());
                    Predicate tagId = criteriaBuilder.equal(root.get("tagId"), commodityTag.getTagId());
                    return criteriaBuilder.and(commodityId, tagId);
                }
        );

        if (target == null) {
            target = new CommodityTag();
        }

        target.setCommodityId(commodityTag.getCommodityId());
        target.setTagId(commodityTag.getTagId());

        target = this.commodityTagRepository.save(target);
        response.setItem(target.getId());

        return response;
    }

    public BaseResponse deleteCommodityTag(Long id) {
        BaseResponse response = new BaseResponse();
        CommodityTag commodityTag = this.commodityTagRepository.findById(id).orElse(null);
        if (commodityTag != null) {
            this.commodityTagRepository.delete(commodityTag);
        }
        return response;
    }

    public BaseResponse deleteCommodityTag(final long commodityId, final long tagId) {
        BaseResponse response = new BaseResponse();
        // this.commodityTagRepository.deleteByCommodityIdAndTagId(commodityId, tagId);
        this.commodityTagRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get("commodityId"),commodityId);
                    Predicate predicate1 = criteriaBuilder.equal(root.get("tagId"), tagId);
                    return criteriaBuilder.and(predicate, predicate1);
                }
        ).ifPresent(x -> this.commodityTagRepository.delete(x));
        return response;
    }
    // endregion

    // region 商品套餐

    public SimpleResponse<Long> storeCommodityFormula(AppRequest<CommodityFormula> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityFormula target = null;
        CommodityFormula formula = request.getBody();
        if (formula.getId() > 0) {
            target = this.commodityFormulaRepository.findById(formula.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(1);
                response.setMessage("要修改的套餐找不到了。");
                return response;
            }
        } else {
            target = new CommodityFormula();
        }
        target.setCommodityId(formula.getCommodityId());
        target.setInventory(formula.getInventory());
        target.setPrice(formula.getPrice());
        target.setTitle(formula.getTitle());

        target = this.commodityFormulaRepository.save(target);
        response.setItem(target.getId());
        return response;
    }


    public BaseResponse deleteCommodityFormula(Long id) {
        BaseResponse response = new BaseResponse();
        this.commodityFormulaRepository.deleteById(id);
        return response;
    }

    public ListResponse<CommodityFormula> listCommodityFormula(Long commodityId) {
        ListResponse<CommodityFormula> response = new ListResponse<>();
        List<CommodityFormula> list =
                this.commodityFormulaRepository.findAllByCommodityIdOrderByIdAsc(commodityId);
        response.setList(list);
        return response;
    }

    // endregion

    // region commodity profile
    public SimpleResponse<Long> storeCommodityProfile(AppRequest<CommodityProfile> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityProfile profile = request.getBody();
        CommodityProfile target = null;
        if (profile.getId() > 0) {
            target = this.commodityProfileRepository.findById(profile.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(1);
                response.setMessage("找不到需要修改的商品信息了。 ");
                return response;
            }
        } else {
            target = new CommodityProfile();
        }
        target.setCommodityId(profile.getCommodityId());
        target.setDetail(profile.getDetail());
        target.setImages(profile.getImages());
        target.setSpecification(profile.getSpecification());
        target = this.commodityProfileRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public BaseResponse deleteCommodityProfile(AppRequest<Long> request) {
        BaseResponse response = new BaseResponse();
        Long id = request.getBody();
        this.commodityProfileRepository.deleteById(id);
        return response;
    }

    public SimpleResponse<CommodityProfile> listCommodityProle(AppRequest<Long> request) {
        SimpleResponse<CommodityProfile> response = new SimpleResponse<>();
        Long commodityId = request.getBody();
        SpecificationFactory<CommodityProfile> factory = new SpecificationFactory<>();
        CommodityProfile profile =
                this.commodityProfileRepository.findOne(factory.equal("commodityId", commodityId))
                        .orElse(null);
        response.setItem(profile);
        return response;
    }
    // endregion

}
