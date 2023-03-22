package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * qq配置
 *
 * @author kuanghuan
 */
@Data
@Component
@ConfigurationProperties("qq")
public class QqConfig {

    private Boolean enable;

    private Long account;

    private String password;

}