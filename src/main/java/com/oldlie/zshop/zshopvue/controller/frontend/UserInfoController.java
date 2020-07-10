package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.db.Wallet;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.UserService;
import com.oldlie.zshop.zshopvue.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CL
 * @date 2020/5/25
 */
@RestController
@RequestMapping("/frontend")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @GetMapping(value = "/wallet/balance")
    public SimpleResponse<Wallet> wallet(@SessionAttribute("uid") long uid) {
        return this.walletService.wallet(uid);
    }

    @GetMapping(value = "/account")
    public SimpleResponse<User> user(@SessionAttribute("uid") long uid) {
        return this.userService.account(uid);
    }

    @PostMapping(value = "/nickname")
    public BaseResponse user(@RequestParam("nickname") String nickname, @SessionAttribute("uid") long uid) {
        return this.userService.updateNickname(uid, nickname);
    }
}
