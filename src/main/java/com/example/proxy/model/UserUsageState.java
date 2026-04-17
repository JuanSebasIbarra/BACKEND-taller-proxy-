package com.example.proxy.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserUsageState {
    private volatile PlanType plan;
    private final AtomicLong monthlyTokensUsed;
    private final AtomicInteger requestsThisMinute;

    public UserUsageState(PlanType plan) {
        this.plan = plan;
        this.monthlyTokensUsed = new AtomicLong(0L);
        this.requestsThisMinute = new AtomicInteger(0);
    }

    public PlanType getPlan() {
        return plan;
    }

    public void setPlan(PlanType plan) {
        this.plan = plan;
    }

    public long getMonthlyTokensUsed() {
        return monthlyTokensUsed.get();
    }

    public int getRequestsThisMinute() {
        return requestsThisMinute.get();
    }

    public void addMonthlyTokens(long tokens) {
        monthlyTokensUsed.addAndGet(tokens);
    }

    public int incrementRequestsThisMinute() {
        return requestsThisMinute.incrementAndGet();
    }

    public void resetRequestsThisMinute() {
        requestsThisMinute.set(0);
    }

    public void resetMonthlyTokensUsed() {
        monthlyTokensUsed.set(0L);
    }
}

