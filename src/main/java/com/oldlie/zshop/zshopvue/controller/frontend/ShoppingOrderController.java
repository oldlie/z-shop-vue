package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderItem;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.ShoppingOrderService;
import lombok.Data;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单处理
 *
 * @author CL
 * @date 2020/5/27
 */
@RestController
@RequestMapping("/frontend")
public class ShoppingOrderController {

    @Autowired
    private ShoppingOrderService service;

    /**
     * 直接下订单
     * @param address post address
     * @param commodityId commodity id
     * @param commodityTitle commodity title
     * @param formulaId formula id
     * @param formulaTitle formula title
     * @param price price
     * @param count count
     * @param uid user id
     * @return serial number
     */
    @PostMapping(value = "/direct-order", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE
    })
    public SimpleResponse<String> directByOrder(@RequestParam("address") String address,
                                                @RequestParam("cid") long commodityId,
                                                @RequestParam("ct") String commodityTitle,
                                                @RequestParam("fid") long formulaId,
                                                @RequestParam("ft") String formulaTitle,
                                                @RequestParam("price") String price,
                                                @RequestParam("count") int count,
                                                @SessionAttribute("uid") long uid) {
        // 直接从购物页面下订单
        return this.service.shoppingOrder(uid, address, commodityId, commodityTitle, formulaId, formulaTitle, count,
                Money.parse(price), 0);
    }

    /**
     * 通过购物车下订单
     */
    public SimpleResponse<String> cartOrder(@RequestBody CartOrderInfo info) {
        return null;
    }

    @Data
    public class CartOrderInfo {
        private String address;
        private List<ShoppingOrderItem> items;
    }

    /**
     * 结算订单，通过订单流水号获取订单中的信息完成订单的结算
     */
    public BaseResponse payOrder(@RequestParam("sn") String serialNumber) {
        return null;
    }

    /**
     * 申请取消订单
     * 1, 订单未支付；
     * 2， 订单未发货；
     * 用户申请之后，订单处于冻结状态
     * @param serialNumber 订单号
     * @return result
     */
    public BaseResponse applyCancelOrder(@RequestParam("sn") String serialNumber) {
        return null;
    }

    /**
     * 取消订单
     * 1. 有支付的返还支付金额
     * @param serialNumber 订单号
     * @return result
     */
    public BaseResponse cancelOrder(@RequestParam("sn") String serialNumber) {
        return null;
    }

    /**
     * 完成订单
     * 1， 用户手动确认订单完成；
     * 2， 超过时长之后自动确认订单完成；
     * @param serialNumber 订单号
     * @return result
     */
    public BaseResponse completeOrder(@RequestParam("sn") String serialNumber) {
        return null;
    }
}
