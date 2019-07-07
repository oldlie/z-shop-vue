package com.oldlie.zshop.zshopvue.model.constant;

import lombok.Data;

@Data
public class CommodityStatus {
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int DELETE = 2;
}
