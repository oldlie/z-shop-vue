package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.ExchangeRecord;
import com.oldlie.zshop.zshopvue.model.db.PayCard;
import com.oldlie.zshop.zshopvue.model.db.PayCardLog;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.PayCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Validated
@RestController
@RequestMapping("/backend")
public class AdminPayCardController {

    private PayCardService payCardService;

    @Autowired
    public void setPayCardService(PayCardService payCardService) {
        this.payCardService = payCardService;
    }

    @PostMapping(value = "/pay-card", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse payCard(@RequestParam("title") String title,
                                @RequestParam("note") String node,
                                @RequestParam("validDay") int validDay,
                                @RequestParam("denomination") String denomination,
                                @RequestParam("amount") String amount,
                                @RequestParam("led") String led,
                                @SessionAttribute("uid") long uid,
                                @SessionAttribute("username") String username) {
        return this.payCardService.payCard(title, node, validDay, denomination, amount, led, uid, username);
    }

    @PostMapping(value = "/pay-cards", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse payCards(@RequestParam("title") String title,
                                 @RequestParam("note") String node,
                                 @RequestParam("validDay") int validDay,
                                 @RequestParam("denomination") String denomination,
                                 @RequestParam("amount") String amount,
                                 @RequestParam("led") String led,
                                 @RequestParam("count") int count,
                                 @SessionAttribute("uid") long uid,
                                 @SessionAttribute("username") String username) {
        return this.payCardService.payCards(title, node, validDay, denomination, amount, led, uid, username, count);
    }

    @DeleteMapping(value = "/pay-card/{id}")
    public BaseResponse delete(@PathVariable("id") long id,
                               @SessionAttribute("uid") long uid,
                               @SessionAttribute("username") String username) {
        return this.payCardService.delete(id, uid, username);
    }

    @GetMapping(value = "/pay-cards/{index}-{size}-{orderBy}-{order}")
    public PageResponse<PayCard> page(@PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("orderBy") String orderBy,
                                      @PathVariable("order") String order) {
        return this.payCardService.page(null, null, index, size, orderBy, order);
    }

    @GetMapping(value = "/pay-cards/{key}/{value}/{index}-{size}-{orderBy}-{order}")
    public PageResponse<PayCard> page(@PathVariable("key") String key,
                                      @PathVariable("value") String value,
                                      @PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("orderBy") String orderBy,
                                      @PathVariable("order") String order) {
        return this.payCardService.page(key, value, index, size, orderBy, order);
    }

    @GetMapping(value = "/pay-card/{id}")
    public SimpleResponse<PayCard> payCard(@PathVariable("id") long id) {
        return this.payCardService.payCard(id);
    }

    /**
     * 出售卡片，
     * <p>如果assign == 1 则表示将卡片直接分配给系统内的账号</p>
     * @param id card id
     * @param customer customer name
     * @param customerPhone customer phone
     * @param amount 售价
     * @param customerId 系统内用户购买
     * @param assign 是否直接分配到系统内账户
     * @param uid user id
     * @param username username
     * @return BaseResponse
     */
    @PostMapping(value = "/pay-card/sold")
    public BaseResponse sell(@RequestParam @NotEmpty String id,
                             @RequestParam @NotEmpty String customer,
                             @RequestParam @NotEmpty String customerPhone,
                             @RequestParam @NotEmpty String amount,
                             @RequestParam long customerId,
                             @RequestParam int assign,
                             @SessionAttribute("uid") long uid,
                             @SessionAttribute("username") String username) {
        return this.payCardService.sold(id, customer, customerPhone, amount, customerId, assign, uid, username);
    }

    /**
     * 获取兑换记录列表
     * @param page page
     * @param size size
     * @return Page of ExchangeRecord
     */
    @GetMapping(value = "/pay-card/exchange")
    public PageResponse<ExchangeRecord> exchangeRecordPageResponse(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size) {
        return this.payCardService.exchangeRecordPage(page, size);
    }

    /**
     * 获取兑换卡操作记录
     * @param page page
     * @param size size
     * @return Page Of Log
     */
    @GetMapping(value = "/pay-card/logs")
    public PageResponse<PayCardLog> payCardLogPageResponse(@RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        return this.payCardService.payCardLogList(page, size);
    }
}
