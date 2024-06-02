package com.pm.trading.notifier.notifier.task;

import com.pm.trading.notifier.notifier.model.MarketType;
import com.pm.trading.notifier.notifier.service.SendMessageService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.concurrent.ScheduledFuture;

@Component
public class AfterMarketTask extends CommonTask implements Runnable {


    public AfterMarketTask(ThreadPoolTaskScheduler taskScheduler, SendMessageService messageService, ConfigurableApplicationContext applicationContext) {
        super(applicationContext, taskScheduler, messageService);
    }

    protected void onRun() {
        messageService.sendMessage("After-market Task is started");

        ScheduledFuture<?> t = taskScheduler.scheduleAtFixedRate(() -> onExecute(MarketType.AFTER_MARKET), Instant.now(), Duration.ofSeconds(10));
        taskScheduler.schedule(() -> {
            messageService.sendMessage("After-market Task is killed");
            t.cancel(false);
            closeDownApplication();
        }, ZonedDateTime.of(LocalDate.now(ZoneId.of("America/New_York")), LocalTime.of(20, 1), ZoneId.of("America/New_York")).toInstant());
    }
}
