package com.oldlie.zshop.zshopvue.service.init.config.sms;

import com.oldlie.zshop.zshopvue.model.db.Config;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.config.ConfigGroup;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class SmsSysVersionConfig extends InitBase<Config> {

    private final Config config;
    @Override
    public Config getEntity() {
        return this.config;
    }

    public SmsSysVersionConfig() {
        this.config = new Config();
        this.config.setKey("SmsSysVersion");
        this.config.setValue("2017-05-25");
        this.config.setTitle("SMS Sys Version");
        this.config.setComment("SDK版本，参考阿里短信SDK实现所需的参数");
        this.config.setGid(ConfigGroup.SMS.getGid());
        this.config.setGroup(ConfigGroup.SMS.getGroup());
    }
}
