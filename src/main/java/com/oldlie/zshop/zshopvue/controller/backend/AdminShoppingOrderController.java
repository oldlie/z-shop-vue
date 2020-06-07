package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrder;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.service.ShoppingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author CL
 * @date 2020/5/28
 */
@RestController
@RequestMapping("/backend")
public class AdminShoppingOrderController {

    @Autowired
    private ShoppingOrderService service;

    /**
     * 获取全部订单信息；
     * 根据SN模糊查询获取订单信息；
     * 根据商品标题模糊查询获取订单信息；
     * 根据SN和商品标题获取订单信息；
     * @param sn serial number
     * @param commodityTitle commodity title
     * @param page page index
     * @param size size
     * @param orderBy order by
     * @param direct direct
     * @return shopping orders
     */
    @GetMapping(value = "/shopping-order-list")
    public PageResponse<ShoppingOrder> page(@RequestParam(name = "sn", required = false, defaultValue = "") String sn,
                                            @RequestParam(name = "ct", required = false, defaultValue = "") String commodityTitle,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam("orderBy") String orderBy,
                                            @RequestParam("direct") String direct,
                                            @SessionAttribute("uid") long uid) {
        return this.service.list(uid, sn, commodityTitle, page, size, orderBy, direct);
    }

    /**
     * 订单发货
     * @param id order id
     * @param postSerialNumber 快递单号
     * @param postCompany  快递公司
     * @param uid user id
     * @return base response
     */
    @PostMapping(value = "/shopping-order/delivering", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_UTF8_VALUE
    })
    public BaseResponse delivery(@RequestParam(name = "id") long id,
                                 @RequestParam(name = "psn") String postSerialNumber,
                                 @RequestParam(name = "pc") String postCompany,
                                 @SessionAttribute("uid") long uid) {
        return this.service.delivery(uid, id, postSerialNumber, postCompany);
    }

    /**
     * 管理员取消订单
     * @param sn order serial number
     * @param reason cancel reason
     * @param uid user id
     * @return base response
     */
    @PostMapping(value = "/shopping-order/cancel", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_UTF8_VALUE
    })
    public BaseResponse cancel(@RequestParam("sn") String sn,
                               @RequestParam("reason") String reason,
                               @SessionAttribute("uid") long uid) {
        return this.service.cancel(uid, sn, reason);
    }
}
