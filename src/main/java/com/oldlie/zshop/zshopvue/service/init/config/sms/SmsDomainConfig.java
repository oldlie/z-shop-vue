package com.oldlie.zshop.zshopvue.service.init.config.sms;

import com.oldlie.zshop.zshopvue.model.db.Config;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.config.ConfigGroup;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class SmsDomainConfig extends InitBase<Config> {

    private final Config config;
    @Override
    public Config getEntity() {
        return this.config;
    }

    public SmsDomainConfig() {
        this.config = new Config();
        this.config.setKey("SmsDomain");
        this.config.setValue("dysmsapi.aliyuncs.com");
        this.config.setTitle("SMS Domain");
        this.config.setComment("短信域，参考阿里短信SDK实现所需的参数");
        this.config.setGid(ConfigGroup.SMS.getGid());
        this.config.setGroup(ConfigGroup.SMS.getGroup());
    }

}
