package com.pm.trading.notifier.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.config")
@Getter
@Setter
public class AppConfig {

    private String scannerUrl;

    private String sendMessageUrl;

    private String chatIdAndText;

    private String mode;
}
