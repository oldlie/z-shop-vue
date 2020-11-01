package com.oldlie.zshop.zshopvue.service.init.config;

import com.oldlie.zshop.zshopvue.model.db.BaseEo;
import com.oldlie.zshop.zshopvue.model.db.Config;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public interface InitConfig<C extends BaseEo> {

    /**
     * Config
     * @return Config
     */
    Config getConfig();
}
