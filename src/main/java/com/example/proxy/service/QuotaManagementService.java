package com.example.proxy.service;

import com.example.proxy.dto.response.QuotaHistoryItemResponse;
import com.example.proxy.dto.response.QuotaStatusResponse;
import com.example.proxy.dto.response.UpgradePlanResponse;
import com.example.proxy.model.DailyUsageEntry;
import com.example.proxy.model.PlanType;
import com.example.proxy.model.UserUsageState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotaManagementService {

    private final UsageTracker usageTracker;

    public QuotaManagementService(UsageTracker usageTracker) {
        this.usageTracker = usageTracker;
    }

    public QuotaStatusResponse getStatus(String userId) {
        UserUsageState state = usageTracker.getRequiredUserState(userId);
        int requestLimit = usageTracker.getRequestLimitPerMinute(state.getPlan());
        long monthlyTokenLimit = usageTracker.getMonthlyTokenLimit(state.getPlan());
        long tokensRemaining = monthlyTokenLimit < 0 ? -1L : Math.max(0L, monthlyTokenLimit - state.getMonthlyTokensUsed());

        return new QuotaStatusResponse(
                state.getPlan().name(),
                state.getMonthlyTokensUsed(),
                tokensRemaining,
                usageTracker.getNextMonthlyResetUtc().toInstant().toString(),
                state.getRequestsThisMinute(),
                requestLimit
        );
    }

    public List<QuotaHistoryItemResponse> getHistory(String userId) {
        List<DailyUsageEntry> entries = usageTracker.getHistory(userId);
        return entries.stream()
                .map(entry -> new QuotaHistoryItemResponse(
                        entry.getDate().toString(),
                        entry.getTokensUsed(),
                        entry.getRequestCount()))
                .toList();
    }

    public UpgradePlanResponse upgradePlan(String userId, String targetPlan) {
        if (!PlanType.PRO.name().equalsIgnoreCase(targetPlan)) {
            throw new IllegalArgumentException("Only upgrade to PRO is allowed");
        }
        PlanType upgradedPlan = usageTracker.upgradeToPro(userId);
        return new UpgradePlanResponse(upgradedPlan.name(), "Plan upgraded successfully");
    }
}

