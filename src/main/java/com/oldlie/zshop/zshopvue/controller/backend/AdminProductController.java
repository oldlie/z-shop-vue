package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/backend")
@RestController
public class AdminProductController {

    private CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @GetMapping(value = "/products/{page}/{size}/{orderBy}/{order}")
    public PageResponse<Commodity> commodities(@PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               @PathVariable("orderBy") String orderBy,
                                               @PathVariable("order") String order) {
        return this.commodityService.commodities(page, size, orderBy, order);
    }

    @GetMapping(value = "/products/{key}/{value}/{page}/{size}/{orderBy}/{order}")
    public PageResponse<Commodity> commodities(@PathVariable("key") String field,
                                               @PathVariable("value") Object value,
                                               @PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               @PathVariable("orderBy") String orderBy,
                                               @PathVariable("order") String order) {
        return this.commodityService.commodities(field, value, page, size, orderBy, order);
    }

    @GetMapping(value = "/product/{id}")
    public SimpleResponse<Commodity> commodity(@PathVariable("id") Long id) {
        return this.commodityService.commodity(id);
    }

    @PostMapping(value = "/product", consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public SimpleResponse<Long> store(@RequestBody Commodity commodity) {
        return this.commodityService.storeCommodity(commodity);
    }

    @DeleteMapping(value = "/product/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        return this.commodityService.deleteCommodity(id);
    }
}
