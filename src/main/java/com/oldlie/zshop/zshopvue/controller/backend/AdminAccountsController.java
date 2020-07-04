package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户账号管理
 * @author oldlie
 * @date 2020/7/4
 */
@RequestMapping("/backend")
@RestController
public class AdminAccountsController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/accounts")
    public PageResponse<User> accounts(@RequestParam("page") int page,
                                       @RequestParam("size") int size,
                                       @RequestParam(value = "orderBy", required = false, defaultValue = "id") String orderBy,
                                       @RequestParam(value = "direct", required = false, defaultValue = "desc") String direct,
                                       @RequestParam(value = "key", required = false, defaultValue = "") String key,
                                       @RequestParam(value = "value", required = false, defaultValue = "") String value) {
        return this.userService.accounts(key, value, page, size, orderBy, direct);
    }

    @GetMapping(value = "/account")
    public SimpleResponse<User> account(@RequestParam("id") long id) {
        return this.userService.account(id);
    }

    @PostMapping(value = "/pay-password")
    public BaseResponse resetPayPassword(@RequestParam("uid") long uid,
                                         @RequestParam("pwd") String password) {
        return this.userService.resetPayPassword(uid, password);
    }

    @PostMapping(value = "/locked")
    public BaseResponse lockedAccount(@RequestParam("uid") long uid) {
        return this.userService.resetAccountStatus(uid, true);
    }

    @PostMapping(value = "/unlock")
    public BaseResponse unlockAccount(@RequestParam("uid") long uid) {
        return this.userService.resetAccountStatus(uid, false);
    }

    @PostMapping(value = "/account")
    public SimpleResponse<User> account(@RequestParam("account") String account,
                                        @RequestParam("password") String password,
                                        @RequestParam("nickname") String nickname,
                                        @RequestParam("payPassword") String payPassword,
                                        @RequestParam("role") String role) {
        return this.userService.account(account, password, nickname, payPassword, role);
    }
}
