package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.PayCard;
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

    @PostMapping(value = "/pay-card/sold")
    public BaseResponse sell(@RequestParam @NotEmpty String id,
                             @RequestParam @NotEmpty String customer,
                             @RequestParam @NotEmpty String customerPhone,
                             @RequestParam @NotEmpty String amount,
                             @SessionAttribute("uid") long uid,
                             @SessionAttribute("username") String username) {
        return this.payCardService.sold(id, customer, customerPhone, amount, uid, username);
    }
}
