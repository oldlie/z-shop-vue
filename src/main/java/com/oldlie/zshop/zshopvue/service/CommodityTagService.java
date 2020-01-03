package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.CommodityTag;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityTagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public void setCommodityTagRepository(CommodityTagRepository repository) {
        this.commodityTagRepository = repository;
    }

    public SimpleResponse<Long> store(final CommodityTag source) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        CommodityTag target;
        if (source.getId() != null && source.getId() > 0) {
            Optional<CommodityTag> optional = this.commodityTagRepository.findOne(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), source.getId())
            );
            if (!optional.isPresent()) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("要修改的记录不存在了。");
                return response;
            }
            target = optional.get();
        } else {
            target = new CommodityTag();
        }
        ObjectCopy<CommodityTag> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(source, target);
        target = this.commodityTagRepository.save(target);
        response.setItem(target.getId());
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
                    Predicate predicateTagid = criteriaBuilder.equal(root.get("tagId"), tagId);
                    return criteriaBuilder.and(predicateCommodityId, predicateTagid);
                }
        ).ifPresent(x -> this.commodityTagRepository.delete(x));
        return response;
    }

    public ListResponse<CommodityTag> list(final long commodityId) {
        ListResponse<CommodityTag> response = new ListResponse<>();
        List<CommodityTag> list = this.commodityTagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("commodityId"), commodityId),
                ZsTool.sort("id", "desc")
        );
        response.setList(list);
        return response;
    }
}
