package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.oldlie.zshop.zshopvue.service.CommodityProfileService;

@RestController
@RequestMapping("/backend/product")
public class AdminCommodityProfileController {

    private CommodityProfileService commodityProfileService;

    @Autowired
    public void setCommodityProfileService(CommodityProfileService commodityProfileService) {
        this.commodityProfileService = commodityProfileService;
    }

    @PostMapping(value = "/profile", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public SimpleResponse<Long> store(@RequestBody CommodityProfile profile) {
        return this.commodityProfileService.store(profile);
    }

    @GetMapping(value = "/profile/{commodityId}")
    public SimpleResponse<CommodityProfile> profile(@PathVariable("commodityId") Long commodityId) {
        return this.commodityProfileService.commodityProfile(commodityId);
    }

    @DeleteMapping(value = "/profile/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        return this.commodityProfileService.delete(id);
    }
}
