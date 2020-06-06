package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.Wallet;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * @author CL
 * @date 2020/5/25
 */
@RestController
@RequestMapping("/frontend")
public class UserInfoController {

    @Autowired
    private WalletService walletService;


    @GetMapping(value = "/wallet/balance")
    public SimpleResponse<Wallet> wallet(@SessionAttribute("uid") long uid) {
        return this.walletService.wallet(uid);
    }

}
