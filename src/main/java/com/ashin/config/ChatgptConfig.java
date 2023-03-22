package com.ashin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * chatgpt配置
 *
 * @author kuanghuan
 */
@Data
@Component
@ConfigurationProperties("chatgpt")
public class ChatgptConfig {

    private List<String> apiKey;
}
