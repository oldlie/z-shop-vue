package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrder;
import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderItem;
import com.oldlie.zshop.zshopvue.model.front.BuyInfo;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.ShoppingOrderService;
import lombok.Data;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
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
     * @param formulaId formula id
     * @param count count
     * @param uid user id
     * @return serial number
     */
    @PostMapping(value = "/direct-order", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public SimpleResponse<String> directByOrder(@RequestParam("address") String address,
                                                @RequestParam("cid") long commodityId,
                                                @RequestParam("fid") long formulaId,
                                                @RequestParam("count") int count,
                                                @SessionAttribute("uid") long uid) throws Exception{
        address = URLDecoder.decode(address, "utf-8");
        // 直接从购物页面下订单
        return this.service.shoppingOrder(uid, address, commodityId, formulaId,  count, 0);
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
    @PostMapping(value = "/shopping-order/pay", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public BaseResponse payOrder(@RequestParam("sn") String serialNumber,
                                 @SessionAttribute("uid") long uid) {
        return this.service.payOrder(uid, serialNumber);
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
    @PostMapping(value = "/shopping-order/cancel", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public BaseResponse cancelOrder(@RequestParam("sn") String serialNumber,
                                    @RequestParam("reason") String reason,
                                    @SessionAttribute("uid") long uid) {
        return this.service.cancel(uid, serialNumber, reason);
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


    @GetMapping(value = "/buy-info/{sn}")
    public SimpleResponse<BuyInfo> buyInfo(@PathVariable("sn") String sn,
                                           @SessionAttribute("uid") long uid) {
        return this.service.buyInfo(uid, sn);
    }


    /**
     * 获取待支付列表
     * @param uid user id
     * @return list
     */
    @GetMapping(value = "/shopping-order/waiting-for-pay")
    public ListResponse<ShoppingOrder> waitingForPay(@SessionAttribute("uid") long uid) {
        return this.service.listForWaitingPay(uid);
    }

    /**
     * 获取正在出库和快递中的订单信息列表
     * @param page page start with 1
     * @param size page size
     * @param uid user id
     * @return list
     */
    @GetMapping(value = "/shopping-order/before-confirmed/{page}/{size}")
    public PageResponse<ShoppingOrder> beforeConfirmed(@PathVariable("page") int page,
                                                       @PathVariable("size") int size,
                                                       @SessionAttribute("uid") long uid) {
        return this.service.listBeforeConfirmed(uid, page, size);
    }

    /**
     * 获取确认收货之后的历史记录
     * @param page page
     * @param size size
     * @param uid user id
     * @return list
     */
    @GetMapping(value = "/shopping-order/history/{page}/{size}")
    public PageResponse<ShoppingOrder> history(@PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               @SessionAttribute("uid") long uid) {
        return this.service.history(uid, page, size, "id", "desc");
    }
}
