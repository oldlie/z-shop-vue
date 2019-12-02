package com.oldlie.zshop.zshopvue.model.constant;

public class ResponseCode {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int EXCEPTION = 2;
    public static final int ERROR = 3;

    public static final String localizationMessage(final int code) {
        String msg = "成功";
        switch (code) {
            case ResponseCode.SUCCESS:
                msg = "成功";
                break;
            case ResponseCode.FAILED:
                msg = "失败";
                break;
            case ResponseCode.EXCEPTION:
                msg = "异常";
                break;
            case ResponseCode.ERROR:
                msg = "错误";
                break;
        }
        return msg;
    }
}
