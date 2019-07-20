package com.oldlie.zshop.zshopvue.model.cs;

public class SHOPPING_ORDER_STATUS {
    // 准备订单
    public final static int PREPARING = 0;
    // 正在出库
    public final static int OUT_OF_WAREHOUSE = 1;
    // 投递中
    public final static int DELIVERING = 2;
    // 已收货
    public final static int RECEIVED = 3;
    // 正在售后
    public final static int AFTER_SALES_SERVICING = 4;
    // 已售后
    public final static int AFTER_SALES_SERVICED = 5;
    // 已退货
    public final static int RETURNED = 6;
    // 已赔付
    public final static int INDEMNITY = 7;
    // 已完成
    public final static int FINISHED = 8;
}
