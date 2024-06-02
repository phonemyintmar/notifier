package com.pm.trading.notifier.notifier.task;

import com.pm.trading.notifier.notifier.model.MarketType;
import com.pm.trading.notifier.notifier.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class PreMarketTask extends CommonTask implements Runnable {

    public PreMarketTask(ThreadPoolTaskScheduler taskScheduler, SendMessageService messageService, ConfigurableApplicationContext applicationContext) {
        super(applicationContext, taskScheduler, messageService);
    }

    protected void onRun() {
        messageService.sendMessage("Pre Market Task is started");

        ScheduledFuture<?> t = taskScheduler.scheduleAtFixedRate(() -> onExecute(MarketType.PRE_MARKET), Instant.now(), Duration.ofSeconds(10));
        taskScheduler.schedule(() -> {
            messageService.sendMessage("Pre Market Task is killed");
            t.cancel(false);
            closeDownApplication();
        }, ZonedDateTime.of(LocalDate.now(ZoneId.of("America/New_York")), LocalTime.of(9, 31), ZoneId.of("America/New_York")).toInstant());
    }
}
