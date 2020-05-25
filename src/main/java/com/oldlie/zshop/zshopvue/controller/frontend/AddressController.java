package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.Address;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author oldlie
 * @date 2020/5/25
 */
@RestController
@RequestMapping("/frontend")
public class AddressController {

    @Autowired
    private AddressService service;

    @PostMapping(value = "/address",
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public SimpleResponse<Long> store(@RequestBody Address address, @SessionAttribute("uid") long uid) {
        return this.service.store(address, uid);
    }

    @DeleteMapping(value = "/address/{id}")
    public BaseResponse delete(@PathVariable("id") long id, @SessionAttribute("uid") long uid) {
        return this.service.delete(id, uid);
    }

    @GetMapping("/addresses")
    public ListResponse<Address> list(@SessionAttribute("uid") long uid) {
        return this.service.listAddress(uid);
    }

    @GetMapping("/default-address")
    public SimpleResponse<Address> defaultAddress(@SessionAttribute("uid") long uid) {
        return this.service.defaultAddress(uid);
    }
}
