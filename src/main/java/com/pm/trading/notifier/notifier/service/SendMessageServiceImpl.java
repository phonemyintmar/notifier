package com.pm.trading.notifier.notifier.service;

import com.pm.trading.notifier.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    private final AppConfig appConfig;

    public SendMessageServiceImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void sendMessage(String message) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appConfig.getSendMessageUrl())
                .append(appConfig.getChatIdAndText())
                .append(message);
        try {
            restTemplate.getForObject(
                    stringBuilder.toString(),
                    Object.class);
            log.info("Successfully sent message: {}", message);
        } catch (Exception e) {
            log.error("Error in sending telegram message");
            log.error("Exception cause => {}", e.getMessage());
            e.printStackTrace();
        }
    }


}
