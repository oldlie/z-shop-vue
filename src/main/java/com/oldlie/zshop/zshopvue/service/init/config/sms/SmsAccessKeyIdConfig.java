package com.oldlie.zshop.zshopvue.service.init.config.sms;

import com.oldlie.zshop.zshopvue.model.db.Config;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.config.ConfigGroup;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class SmsAccessKeyIdConfig extends InitBase<Config> {

    private final Config config;
    @Override
    public Config getEntity() {
        return this.config;
    }

    public SmsAccessKeyIdConfig() {
        this.config = new Config();
        this.config.setKey("SmsAccessKeyId");
        this.config.setValue("");
        this.config.setTitle("SMS Access Key Id");
        this.config.setComment("ACCESS KEY，请小心保管不要泄露");
        this.config.setGid(ConfigGroup.SMS.getGid());
        this.config.setGroup(ConfigGroup.SMS.getGroup());
    }

}
