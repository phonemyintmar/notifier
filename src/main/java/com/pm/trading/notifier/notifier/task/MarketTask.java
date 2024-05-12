package com.pm.trading.notifier.notifier.task;

import com.pm.trading.notifier.notifier.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.lang.module.Configuration;
import java.time.*;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class MarketTask extends CommonTask implements Runnable {
    private final ConfigurableApplicationContext applicationContext;

    public MarketTask(ThreadPoolTaskScheduler taskScheduler, SendMessageService messageService, ConfigurableApplicationContext applicationContext) {
        super(taskScheduler, messageService);
        this.applicationContext = applicationContext;
    }

    protected void onRun() {
        messageService.sendMessage("Market Task is started");

        ScheduledFuture<?> t = taskScheduler.scheduleAtFixedRate(() -> onExecute(false), Instant.now(), Duration.ofSeconds(10));
        taskScheduler.schedule(() -> {
            messageService.sendMessage("Market Task is killed");
            t.cancel(false);
            applicationContext.close();
        }, ZonedDateTime.of(LocalDate.now(ZoneId.of("America/New_York")), LocalTime.of(16, 1), ZoneId.of("America/New_York")).toInstant());
    }
}
