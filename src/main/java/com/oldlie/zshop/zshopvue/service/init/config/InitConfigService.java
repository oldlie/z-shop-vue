package com.oldlie.zshop.zshopvue.service.init.config;

import com.oldlie.zshop.zshopvue.model.db.repository.ConfigRepository;
import com.oldlie.zshop.zshopvue.service.init.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/11/1
 */
@Service
@Slf4j
public class InitConfigService extends InitService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public void init() {
        setRepository();
        this.init(new SmsDomainConfig());
    }

    @Override
    public void setRepository() {
        this.repository = this.configRepository;
    }
}
