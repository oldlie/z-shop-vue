package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.CommodityTag;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityTagRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.TagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━━━━━━━
 * 2020/01/03
 * CommodityTagService
 *
 * @author 陈列
 */
@Service
@Slf4j
public class CommodityTagService {

    private CommodityTagRepository commodityTagRepository;
    private TagRepository tagRepository;

    @Autowired
    public void setCommodityTagRepository(CommodityTagRepository repository) {
        this.commodityTagRepository = repository;
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public SimpleResponse<Long> store(final long commodityId,
                                      final long tagId) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Optional<CommodityTag> optional = this.commodityTagRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicateCommodityId = criteriaBuilder.equal(root.get("commodityId"), commodityId);
                    Predicate predicateTagId = criteriaBuilder.equal(root.get("tagId"), tagId);
                    return criteriaBuilder.and(predicateCommodityId, predicateTagId);
                }
        );
        if (optional.isPresent()) {
            response.setItem(optional.get().getId());
        } else {
            CommodityTag commodityTag = new CommodityTag();
            commodityTag.setCommodityId(commodityId);
            commodityTag.setTagId(tagId);
            commodityTag = this.commodityTagRepository.save(commodityTag);
            response.setItem(commodityTag.getId());
        }
        return response;
    }

    public BaseResponse delete(final long id) {
        BaseResponse response = new BaseResponse();
        this.commodityTagRepository.findById(id).ifPresent(x -> this.commodityTagRepository.delete(x));
        return response;
    }

    public BaseResponse delete(final long commodityId, final long tagId) {
        BaseResponse response = new BaseResponse();
        this.commodityTagRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicateCommodityId =
                            criteriaBuilder.equal(root.get("commodityId"), commodityId);
                    Predicate predicateTagId = criteriaBuilder.equal(root.get("tagId"), tagId);
                    return criteriaBuilder.and(predicateCommodityId, predicateTagId);
                }
        ).ifPresent(x -> this.commodityTagRepository.delete(x));
        return response;
    }

    @Transactional
    public ListResponse<Tag> list(final long commodityId) {
        ListResponse<Tag> response = new ListResponse<>();
        List<CommodityTag> list = this.commodityTagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("commodityId"), commodityId),
                ZsTool.sort("id", "desc")
        );

        if (list.size() <= 0) {
            return response;
        }

        List<Tag> tags = this.tagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("id"));
                    for (CommodityTag commodityTag : list) {
                        in.value(commodityTag.getTagId());
                    }
                    return in;
                }
        );
        response.setList(tags);

        return response;
    }
}
