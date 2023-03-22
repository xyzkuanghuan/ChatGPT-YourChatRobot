package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置
 *
 * @author kuanghuan
 */
@Data
@Component
@ConfigurationProperties("wechat")
public class WechatConfig {

    private Boolean enable;

    private String qrPath;

}