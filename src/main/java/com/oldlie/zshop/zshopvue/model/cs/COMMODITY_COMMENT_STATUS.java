package com.oldlie.zshop.zshopvue.model.cs;

/**
 * 用户评论的状态
 */
public class COMMODITY_COMMENT_STATUS {
    /**
     * 用户提交商品评价
     */
    public static int INIT_ = 0;
    /**
     * 管理员通过了用户评价，这时可以在前端展示
     */
    public static int PASS_ = 1;
    /**
     * 管理员无效了用户的评价，不在评论区展示
     */
    public static int FAIL_ = 2;
}
