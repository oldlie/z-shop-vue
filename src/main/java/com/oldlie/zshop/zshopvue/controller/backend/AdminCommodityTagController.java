package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.db.CommodityTag;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.CommodityTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━━━━━━━
 * 2020/01/03
 * AdminCommodityTagController
 *
 * @author 陈列
 */
@RestController
@RequestMapping("/backend/product")
public class AdminCommodityTagController {

    private CommodityTagService commodityTagService;

    @Autowired
    public void setCommodityTagService(CommodityTagService commodityTagService) {
        this.commodityTagService = commodityTagService;
    }

    @PostMapping(value = "/tag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SimpleResponse<Long> store(@RequestParam(value = "commodityId") long commodityId,
                                      @RequestParam(value = "tagId") long tagId) {
        CommodityTag tag = new CommodityTag();
        tag.setCommodityId(commodityId);
        tag.setTagId(tagId);
        return this.commodityTagService.store(tagId, commodityId);
    }

    @DeleteMapping(value = "/tag/{id}")
    public BaseResponse delete(@PathVariable(value = "id") long id) {
        return this.commodityTagService.delete(id);
    }

    @DeleteMapping(value = "/tag/{commodityId}/{tagId}")
    public BaseResponse delete(@PathVariable(value = "commodityId") long commodityId,
                               @PathVariable(value = "tagId") long tagId) {
        return this.commodityTagService.delete(commodityId, tagId);
    }

    @GetMapping(value = "/tags/{commodityId}")
    public ListResponse<Tag> list(@PathVariable(value = "commodityId") long commodityId) {
        return this.commodityTagService.list(commodityId);
    }
}
