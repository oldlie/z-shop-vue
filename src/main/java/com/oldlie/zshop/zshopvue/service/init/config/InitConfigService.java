package com.oldlie.zshop.zshopvue.service.init.config;

import com.oldlie.zshop.zshopvue.model.db.Config;
import com.oldlie.zshop.zshopvue.model.db.repository.ConfigRepository;
import com.oldlie.zshop.zshopvue.service.init.InitBase;
import com.oldlie.zshop.zshopvue.service.init.InitService;
import com.oldlie.zshop.zshopvue.service.init.config.sms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/11/1
 */
@Service
@Slf4j
public class InitConfigService extends InitService<Config, InitBase<Config>, Long, ConfigRepository> {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public void init() {
        this.init(new SmsAccessKeyIdConfig())
                .init(new SmsAccessSecretConfig())
                .init(new SmsDomainConfig())
                .init(new SmsRegionIdConfig())
                .init(new SmsSignNameConfig())
                .init(new SmsSysVersionConfig())
                .init(new SmsTemplateCodeConfig());
    }

    @Override
    public void setRepository() {
        this.repository = this.configRepository;
    }
}
