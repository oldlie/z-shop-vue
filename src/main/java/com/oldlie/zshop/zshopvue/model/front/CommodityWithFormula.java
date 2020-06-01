package com.oldlie.zshop.zshopvue.model.front;

import lombok.Data;

@Deprecated
@Data
public class CommodityWithFormula {
    private long commodityId;
    private String commodityTitle;
    private long formulaId;
    private String formulaTitle;
    private String price;
    private int count;
}
