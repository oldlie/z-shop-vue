package com.oldlie.zshop.zshopvue.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "zs.system")
@Component
@Data
@ToString
@EqualsAndHashCode
public class SystemProperties {
    private String fileUploadTemp;
    private String uploadFileUrl;
}
