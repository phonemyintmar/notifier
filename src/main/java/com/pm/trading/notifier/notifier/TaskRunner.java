package com.pm.trading.notifier.notifier;

import com.pm.trading.notifier.notifier.service.SendMessageService;
import com.pm.trading.notifier.notifier.task.MarketTask;
import com.pm.trading.notifier.notifier.task.PreMarketTask;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TaskRunner {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final SendMessageService messageService;
    private final PreMarketTask preMarketTask;
    private final MarketTask marketTask;

    public TaskRunner(ThreadPoolTaskScheduler taskScheduler, SendMessageService messageService, PreMarketTask preMarketTask, MarketTask marketTask) {
        this.taskScheduler = taskScheduler;
        this.messageService = messageService;
        this.preMarketTask = preMarketTask;
        this.marketTask = marketTask;
    }

    @PostConstruct
    public void afterConstruct() {
        messageService.sendMessage("App is started!");
        run();
    }

    private void run() {
        taskScheduler.schedule(preMarketTask, ZonedDateTime.of(LocalDate.now(ZoneId.of("America/New_York")), LocalTime.of(4, 0), ZoneId.of("America/New_York")).toInstant());
        taskScheduler.schedule(marketTask, ZonedDateTime.of(LocalDate.now(ZoneId.of("America/New_York")), LocalTime.of(9, 30), ZoneId.of("America/New_York")).toInstant());

    }

}
