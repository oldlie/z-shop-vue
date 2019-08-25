package com.oldlie.zshop.zshopvue.controller;

import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.front.FrontUser;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.UserService;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/quite")
    @ResponseBody
    public BaseResponse logout() {
        BaseResponse response = new BaseResponse();
        return response;
    }

    @GetMapping("/name")
    @ResponseBody
    public SimpleResponse<FrontUser> currentUser(HttpSession session) {
        SimpleResponse<FrontUser> response = new SimpleResponse<>();
        Object o = session.getAttribute("username");
        String name = o == null ? "" : o.toString();
        FrontUser target = new FrontUser();
        if (StringUtils.isNotEmpty(name)) {
            User user = this.userService.user(name);
            target.setId(user.getId());
            target.setUsername(user.getUsername());
            target.setRoles(user.getRoles());
        }
        response.setItem(target);
        return response;
    }
}
