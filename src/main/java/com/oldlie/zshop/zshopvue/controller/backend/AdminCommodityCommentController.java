package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.CommodityComment;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.service.CommodityCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class AdminCommodityCommentController {

    @Autowired
    private CommodityCommentService ccService;

    @GetMapping(value = "/comments/new/top10")
    public ListResponse<CommodityComment> top10Comments() {
        return this.ccService.top10Comments(0);
    }

    @GetMapping(value = "/comments/fail/top10")
    public ListResponse<CommodityComment> top10FailedComments() {
        return this.ccService.top10Comments(2);
    }

    @PostMapping(value = "/comment/pass")
    public BaseResponse passComment(@RequestParam("id") long id) {
        return this.ccService.passComment(id);
    }

    @PostMapping(value = "/comment/fail")
    public BaseResponse failComment(@RequestParam("id") long id) {
        return this.ccService.failedComment(id);
    }
}
