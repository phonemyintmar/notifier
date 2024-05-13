package com.pm.trading.notifier.notifier.task;

import com.pm.trading.notifier.notifier.model.TradingData;
import com.pm.trading.notifier.notifier.payload.ScannerRequest;
import com.pm.trading.notifier.notifier.payload.ScannerResponse;
import com.pm.trading.notifier.notifier.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CommonTask implements Runnable {
    protected final ThreadPoolTaskScheduler taskScheduler;
    protected final SendMessageService messageService;

    Map<String, TradingData> oldMap;
    Map<String, TradingData> newMap;

    @Value("${app.config.scannerUrl}")
    public String SCANNER_URL; //dr ka a mhan takl appConfig ko import pee use ll ya dl but
    // abstract so tok implementing class twy mhr pr lite thwin ny ya mhr soe loh shote loh

    @Value("${screener.one.url}")
    public String SCREENR_ONE_URL;

    @Value("${screener.two.url}")
    public String SCREENR_TWO_URL;

    public static final String ERROR_API_MSG = "There was an error in calling scanner API";

    public CommonTask(ThreadPoolTaskScheduler taskScheduler, SendMessageService messageService) {
        oldMap = new HashMap<>();
        newMap = new HashMap<>();
        this.taskScheduler = taskScheduler;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        onRun();
    }

    protected abstract void onRun();

    protected void onExecute(boolean isPremarket) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<ScannerResponse> response = restTemplate.postForEntity(SCANNER_URL,
                    new ScannerRequest(isPremarket), ScannerResponse.class);
            for (ScannerResponse.Data data : response.getBody().getData()) {
                ArrayList<Object> tradingDataRaw = data.getD();
                TradingData tradingData = new TradingData();
                tradingData.setTicker((String) tradingDataRaw.get(0));
                tradingData.setVolume((Integer) tradingDataRaw.get(4));
                tradingData.setPreVolume((Integer) tradingDataRaw.get(3));
                tradingData.setCurrentPrice(castDouble(tradingDataRaw.get(1)));
                tradingData.setIncreasePercentage((Double) tradingDataRaw.get(2));
                newMap.put(tradingData.getTicker(), tradingData);
            }
        } catch (Exception e) {
            log.error(ERROR_API_MSG);
            messageService.sendMessage(ERROR_API_MSG);
        }

        if (!oldMap.isEmpty()) {
            for (Map.Entry<String, TradingData> entry : newMap.entrySet()) {
                String key = entry.getKey();
                TradingData newData = entry.getValue();
                if (!oldMap.containsKey(key)) {
                    messageService.sendMessage(createMessage(newData, true));
                } else {
                    TradingData oldData = oldMap.get(key);
                    if (checkValidity(newData, oldData, isPremarket)) {
                        messageService.sendMessage(createMessage(newData, false));
                    }
                }
            }
        }
        oldMap = newMap;
        newMap = new HashMap<>();
    }

    // a mhan ka all value ko double cast ml so yin generic ka m lo wo just use Int.value of br nyr,
    // but it's cool lOLL
    protected Double castDouble(Object value) {
        Number numberValue = (Number) value;

        if (value instanceof Integer) {
            int intValue = numberValue.intValue();
            return (double) intValue;
        } else if (value instanceof Double) {
            return numberValue.doubleValue();
        } else {
            log.error("Unknown number type");
            return 0.0;
        }
    }

    protected boolean checkValidity(TradingData newData, TradingData oldData, boolean isPremarket) {
        if (isPremarket) {
            return (newData.getIncreasePercentage() - oldData.getIncreasePercentage() > 10 && newData.getPreVolume() > 10000);
        } else {
            return (newData.getIncreasePercentage() - oldData.getIncreasePercentage() > 10 && newData.getVolume() > 500000);
        }
    }

    protected String createMessage(TradingData tradingData, boolean isNew) {
        //out ka hr ka intellij ka recommend tr, text block nae replace pr lrr so loh. So we just kept both versions.
        //well now it is gone because I used variables basically it was something like triple """ and \s things
        if (isNew) {
            return String.format("There is a new stock to look out. It just got into the list. \n" +
                            "Name - %s \n" +
                            SCREENR_ONE_URL + "%s \n" +
                            SCREENR_TWO_URL,
                    tradingData.getTicker(),
                    tradingData.getTicker());
        } else {
            return String.format("There is a new stock to look out. It just went up more than 10 percent. \n" +
                            "Name - %s \n" +
                            SCREENR_ONE_URL + "%s \n" +
                            SCREENR_TWO_URL,
                    tradingData.getTicker(),
                    tradingData.getTicker());
        }

    }
}
