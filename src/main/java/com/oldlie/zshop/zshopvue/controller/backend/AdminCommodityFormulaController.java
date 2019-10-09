package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.CommodityFormula;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CommodityFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/product")
public class AdminCommodityFormulaController {

    private CommodityFormulaService commodityFormulaService;

    @Autowired
    public void setCommodityFormulaService(CommodityFormulaService commodityFormulaService) {
        this.commodityFormulaService = commodityFormulaService;
    }

    @PostMapping(value = "/formula", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public SimpleResponse<Long> store(@RequestBody CommodityFormula formula) {
        return this.commodityFormulaService.store(formula);
    }

    @GetMapping(value = "/formula-list/{commodityId}")
    public ListResponse<CommodityFormula> list(@PathVariable("commodityId") Long id) {
        return this.commodityFormulaService.commodityFormulaList(id);
    }

    @DeleteMapping(value = "formula/{id}")
    public BaseResponse delete(@PathVariable("id") Long id) {
        return this.commodityFormulaService.delete(id);
    }
}
