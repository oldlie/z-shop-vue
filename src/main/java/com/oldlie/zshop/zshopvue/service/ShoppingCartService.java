package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.ShoppingCart;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.db.repository.ShoppingCartRepository;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author CL
 * @date 2020/5/26
 */
@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository repository;

    public BaseResponse store(long uid, ShoppingCart source) {
        BaseResponse response = new BaseResponse();

        if (source.getUid() == null || uid != source.getUid() || source.getCommodityId() == null || source.getFormulaId() == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("数据格式不正确");
            return response;
        }

        ShoppingCart target = this.repository.findOneByUidAndCommodityIdAndFormulaId(uid,
                source.getCommodityId(),
                source.getFormulaId());

        int count = 0;
        if (target == null) {
            target = new ShoppingCart();
        } else {
            count = target.getCount();
        }

        ObjectCopy<ShoppingCart> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(source, target);
        target.setCount(count + target.getCount());
        this.repository.save(target);

        return response;
    }

    public BaseResponse delete(long id) {
        this.repository.deleteById(id);
        return new BaseResponse();
    }

    public BaseResponse delete(long uid, long commodityId, long formulaId) {
        this.repository.deleteByUidAndCommodityIdAndFormulaId(uid, commodityId, formulaId);
        return new BaseResponse();
    }

    public PageResponse<ShoppingCart> page(long uid, int pageIndex, int size, String orderBy, String direct) {
        PageResponse<ShoppingCart> response = new PageResponse<>();
        Page<ShoppingCart> page = this.repository.findAllByUid(uid, ZsTool.pageable(pageIndex, size, orderBy, orderBy));
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }
}
