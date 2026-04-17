package com.example.proxy.scheduler;

import com.example.proxy.service.UsageTracker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UsageResetScheduler {

    private final UsageTracker usageTracker;

    public UsageResetScheduler(UsageTracker usageTracker) {
        this.usageTracker = usageTracker;
    }

    @Scheduled(fixedRate = 60_000L)
    public void resetMinuteCounters() {
        usageTracker.resetMinuteCounters();
    }

    @Scheduled(cron = "0 0 0 1 * *", zone = "UTC")
    public void resetMonthlyQuota() {
        usageTracker.resetMonthlyUsage();
    }
}

