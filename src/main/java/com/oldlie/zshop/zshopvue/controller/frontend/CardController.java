package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.PayCard;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.PayCardService;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author CL
 * @date 2020/5/25
 */
@RestController
@RequestMapping("/frontend")
public class CardController {

    @Autowired
    private PayCardService service;

    @RequestMapping(value = "/card/exchange", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.APPLICATION_JSON_UTF8_VALUE
    })
    public SimpleResponse<Money> exchange(@RequestParam String sn,
                                          @RequestParam String vc,
                                          @SessionAttribute("uid") long uid,
                                          @SessionAttribute("username") String username) {
        return this.service.payCardRecharge(uid, username, sn, vc);
    }

    @GetMapping("/card/my-cards/{page}/{size}/{orderBy}/{direct}")
    public PageResponse<PayCard> myCards(@PathVariable("page") int page,
                                         @PathVariable("size") int size,
                                         @PathVariable("orderBy") String orderBy,
                                         @PathVariable("direct") String direct,
                                         @SessionAttribute("uid") long uid) {
        return this.service.listMyPayCards(uid, page,size, orderBy, direct);
    }

    @GetMapping(value = "/card/my-valid-cards")
    public PageResponse<PayCard> myValidCards(@RequestParam("page") int page,
                                              @RequestParam("size") int size,
                                              @SessionAttribute("uid") long uid) {
        return this.service.customerPayCards(uid, page, size);
    }

    @PostMapping(value = "/card/invalid")
    public BaseResponse invalidCard(@RequestParam("sn") String sn,
                                    @RequestParam("vc") String vc,
                                    @SessionAttribute("uid") long uid,
                                    @SessionAttribute("username") String username) {
        return this.service.invalidCard(uid, username, sn, vc);
    }
}
