package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.exception.UserNotFoundException;
import com.example.proxy.model.DailyUsageEntry;
import com.example.proxy.model.PlanLimits;
import com.example.proxy.model.PlanType;
import com.example.proxy.model.UserUsageState;
import com.example.proxy.repository.UsageRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class UsageTracker {

    private static final int HISTORY_DAYS = 7;

    private final UsageRepository usageRepository;
    private final AppProperties appProperties;
    private final Clock clock;

    public UsageTracker(UsageRepository usageRepository, AppProperties appProperties) {
        this.usageRepository = usageRepository;
        this.appProperties = appProperties;
        this.clock = Clock.systemUTC();
    }

    public UserUsageState getOrCreateUserState(String userId) {
        return usageRepository.getOrCreateUserState(userId);
    }

    public UserUsageState getRequiredUserState(String userId) {
        return usageRepository.findUserState(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    public int getRequestLimitPerMinute(PlanType planType) {
        PlanLimits limits = appProperties.getPlanLimits(planType);
        return limits.unlimited() ? -1 : limits.requestsPerMinute();
    }

    public long getMonthlyTokenLimit(PlanType planType) {
        PlanLimits limits = appProperties.getPlanLimits(planType);
        return limits.unlimited() ? -1L : limits.monthlyTokens();
    }

    public boolean tryAcquireRequestSlot(String userId) {
        UserUsageState state = getOrCreateUserState(userId);
        PlanLimits limits = appProperties.getPlanLimits(state.getPlan());
        if (limits.unlimited()) {
            state.incrementRequestsThisMinute();
            recordDailyUsage(userId, 0, true);
            return true;
        }

        synchronized (state) {
            if (state.getRequestsThisMinute() >= limits.requestsPerMinute()) {
                return false;
            }
            state.incrementRequestsThisMinute();
            recordDailyUsage(userId, 0, true);
            return true;
        }
    }

    public boolean tryConsumeMonthlyTokens(String userId, int tokens) {
        UserUsageState state = getOrCreateUserState(userId);
        PlanLimits limits = appProperties.getPlanLimits(state.getPlan());
        if (limits.unlimited()) {
            recordDailyUsage(userId, tokens, false);
            return true;
        }

        synchronized (state) {
            long projectedUsage = state.getMonthlyTokensUsed() + tokens;
            if (projectedUsage > limits.monthlyTokens()) {
                return false;
            }
            state.addMonthlyTokens(tokens);
            recordDailyUsage(userId, tokens, false);
            return true;
        }
    }

    public List<DailyUsageEntry> getHistory(String userId) {
        getRequiredUserState(userId);
        return usageRepository.findLastDailyEntries(userId, HISTORY_DAYS);
    }

    public PlanType upgradeToPro(String userId) {
        UserUsageState state = getOrCreateUserState(userId);
        state.setPlan(PlanType.PRO);
        return state.getPlan();
    }

    public int getSecondsUntilMinuteReset() {
        Instant now = clock.instant();
        long epoch = now.getEpochSecond();
        long passed = epoch % 60;
        return (int) (60 - passed);
    }

    public ZonedDateTime getNextMonthlyResetUtc() {
        ZonedDateTime utcNow = ZonedDateTime.now(clock).withZoneSameInstant(ZoneOffset.UTC);
        return utcNow.plusMonths(1)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public void resetMinuteCounters() {
        usageRepository.getAllUserStates().values().forEach(UserUsageState::resetRequestsThisMinute);
        pruneOldHistory();
    }

    public void resetMonthlyUsage() {
        usageRepository.getAllUserStates().values().forEach(UserUsageState::resetMonthlyTokensUsed);
        pruneOldHistory();
    }

    private void recordDailyUsage(String userId, long tokens, boolean incrementRequest) {
        LocalDate today = LocalDate.now(clock);
        DailyUsageEntry entry = usageRepository.getOrCreateDailyEntry(userId, today);
        if (incrementRequest) {
            entry.incrementRequestCount();
        }
        if (tokens > 0) {
            entry.addTokens(tokens);
        }
    }

    private void pruneOldHistory() {
        LocalDate cutoff = LocalDate.now(clock).minusDays(HISTORY_DAYS - 1L);
        usageRepository.pruneHistoryBefore(cutoff);
    }
}




