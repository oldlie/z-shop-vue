package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.EvaluativeDimension;
import com.oldlie.zshop.zshopvue.model.db.UserCommodityEva;
import com.oldlie.zshop.zshopvue.model.db.repository.EvaDimRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.UserCommodityEvaRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

/**
 * 处理用户评价商品的维度
 */
@Service
@Slf4j
public class EvaDimService {

    @Autowired
    private EvaDimRepository evaDimRepository;

    @Autowired
    private UserCommodityEvaRepository uceRepository;

    public SimpleResponse<Long> store(EvaluativeDimension source) {
        SimpleResponse<Long> response = new SimpleResponse<>();

        if (source.getCid() <= 0) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("非法访问");
            return response;
        }

        long count = this.evaDimRepository.count(
                (root, criteriaQuery, cb) -> cb.equal(root.get("cid"), source.getCid())
        );
        if (count >= 5) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("一件商品最多允许设置5个用户评价维度");
            return response;
        }

        EvaluativeDimension target;
        if (source.getId() == null || source.getId() <= 0L) {
            target = new EvaluativeDimension();
        } else {
            Optional<EvaluativeDimension> optional = this.evaDimRepository.findById(source.getId());
            if (!optional.isPresent()) {
                 response.setStatus(HTTP_CODE.FAILED);
                 response.setMessage("这条评价不存在了");
                 return response;
            }
            target = optional.get();
        }
        ObjectCopy<EvaluativeDimension> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(source, target);
        target = this.evaDimRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public BaseResponse delete(long id) {
        BaseResponse response = new BaseResponse();
        Optional<EvaluativeDimension> optional = this.evaDimRepository.findById(id);
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这条评价不存在了");
            return response;
        }
        this.evaDimRepository.delete(optional.get());
        return response;
    }

    public ListResponse<EvaluativeDimension> list(long cid) {
        ListResponse<EvaluativeDimension> response = new ListResponse<>();
        List<EvaluativeDimension> list = this.evaDimRepository.findAll(
                (root, criteriaQuery, cb) -> cb.equal(root.get("cid"), cid),
                ZsTool.sort("id", "desc")
        );
        response.setList(list);
        return response;
    }

    /**
     * 记录用户每个订单中对每件商品的评价打分
     * @param eva user commodity evaluation
     * @return BaseResponse
     */
    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<Integer> evaluation(UserCommodityEva eva) {
        SimpleResponse<Integer> response = new SimpleResponse<>();

        Optional<UserCommodityEva> optional1 = this.uceRepository.findOne((root, criteriaQuery, cb) -> {
            Predicate predicate = cb.equal(root.get("uid"), eva.getUid());
            Predicate predicate2 = cb.equal(root.get("eid"), eva.getEid());
            Predicate predicate1 = cb.equal(root.get("oid"), eva.getOid());
            return cb.and(predicate, predicate1, predicate2);
        });
        if (optional1.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这件商品本次购物已经打过分了");
            response.setItem(optional1.get().getCount());
            return response;
        }

        Optional<EvaluativeDimension> optional = this.evaDimRepository.findOne(
                (root, criteriaQuery, cb) -> {
                    Predicate predicate = cb.equal(root.get("id"), eva.getId());
                    Predicate predicate1 = cb.equal(root.get("cid"), eva.getCid());
                    return cb.and(predicate, predicate1);
                }
        );
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这个评价被取消了，请刷新页面重试");
            return response;
        }
        EvaluativeDimension dimension = optional.get();
        dimension.setCount(dimension.getCount() + 1);
        dimension.setTotal(dimension.getTotal() + eva.getCount());
        this.evaDimRepository.save(dimension);

        // 记录用户的每一次打分
        this.uceRepository.save(eva);
        response.setItem(eva.getCount());
        return response;
    }

    /**
     * 获取商品的平均得分，计算方法 (total * 2 * 100) / count;前端使用的主意需要将这个分值除以100
     * @param cid commodity id
     * @return 得分
     */
    public SimpleResponse<Long> averageScore(long cid) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        List<EvaluativeDimension> list = this.evaDimRepository.findAll(
                (root, criteriaQuery, cb) -> cb.equal(root.get("cid"), cid)
        );
        if (list.size() <= 0) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("没有设置评价维度");
            return response;
        }
        long total = 0;
        long count = 0;
        for (EvaluativeDimension dimension : list) {
            total += dimension.getTotal();
            count += dimension.getCount();
        }
        Long result = ((total * 2) * 100) / count;
        response.setItem(result);
        return response;
    }
}

