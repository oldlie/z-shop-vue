package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.FinancialAccount;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.service.FinancialAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlie
 * @date 2020/7/12
 */
@RestController
@RequestMapping("/backend")
public class AdminFinancialAccountController {

    @Autowired
    private FinancialAccountService faService;

    @GetMapping(value = "/financial/list")
    public PageResponse<FinancialAccount> list(@RequestParam("page") int page,
                                               @RequestParam("size") int size) {
        return this.faService.list(page, size);
    }
}
