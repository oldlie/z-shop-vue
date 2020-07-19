package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.EvaluativeDimension;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.EvaDimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员设置商品的评价维度
 */
@RestController
@RequestMapping("/backend" )
public class AdminEvaDimController {

    @Autowired
    private EvaDimService evaDimService;

    @GetMapping("/dimensions")
    public ListResponse<EvaluativeDimension> list(@RequestParam("cid") long cid) {
        return this.evaDimService.list(cid);
    }

    @PostMapping("/dimension")
    public SimpleResponse<Long> store(@RequestParam("cid") long cid,
                                      @RequestParam("title") String title) {
        EvaluativeDimension dimension = EvaluativeDimension.builder()
                .cid(cid)
                .title(title)
                .count(1)
                .total(5)
                .build();
        return this.evaDimService.store(dimension);
    }

    @DeleteMapping("/dimension")
    public BaseResponse delete(@RequestParam("id") long id) {
        return this.evaDimService.delete(id);
    }
}
