package com.oldlie.zshop.zshopvue.service.init.config.sms;

import com.oldlie.zshop.zshopvue.model.db.Config;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.config.ConfigGroup;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class SmsTemplateCodeConfig extends InitBase<Config> {


    private final Config config;
    @Override
    public Config getEntity() {
        return this.config;
    }

    public SmsTemplateCodeConfig() {
        this.config = new Config();
        this.config.setKey("SmsTemplateCodeConfig");
        this.config.setValue("");
        this.config.setTitle("模板代码");
        this.config.setComment("参考阿里短信SDK实现所需的参数");
        this.config.setGid(ConfigGroup.SMS.getGid());
        this.config.setGroup(ConfigGroup.SMS.getGroup());
    }

}
