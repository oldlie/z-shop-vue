package com.oldlie.zshop.zshopvue.controller.frontend;

import com.oldlie.zshop.zshopvue.model.db.Address;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**
 * @author oldlie
 * @date 2020/5/25
 */
@RestController
@RequestMapping("/frontend")
public class AddressController {

    @Autowired
    private AddressService service;

    private static final String utf8 = "utf-8";

    @PostMapping(value = "/address", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public SimpleResponse<Long> store(@RequestParam(name = "id", required = false, defaultValue = "0") long id,
                                      @RequestParam("pa") String name,
                                      @RequestParam("phone") String phone,
                                      @RequestParam("address") String address,
                                      @SessionAttribute("uid") long uid) throws Exception {
        name = URLDecoder.decode(name, utf8);
        phone = URLDecoder.decode(phone, utf8);
        address = URLDecoder.decode(address, utf8);
        Address addr = Address.builder()
                        .uid(uid)
                        .isDefault(0)
                        .province("")
                        .city("")
                        .county("")
                        .address(address)
                        .contactName(name)
                        .contactPhone(phone)
                        .info("")
                        .build();
        addr.setId(id);
        return this.service.store(addr, uid);
    }

//    @PostMapping(value = "/address",
//            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public SimpleResponse<Long> store(@RequestBody Address address, @SessionAttribute("uid") long uid) {
//        return this.service.store(address, uid);
//    }

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

    @PostMapping(value = "/default-address", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public BaseResponse defaultAddress(@RequestParam("id") long id,
                                       @SessionAttribute("uid") long uid) {
        return this.service.doDefault(id, uid);
    }
}
