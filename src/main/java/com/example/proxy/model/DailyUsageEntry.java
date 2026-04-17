package com.example.proxy.model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DailyUsageEntry {
    private final LocalDate date;
    private final AtomicLong tokensUsed;
    private final AtomicInteger requestCount;

    public DailyUsageEntry(LocalDate date) {
        this.date = date;
        this.tokensUsed = new AtomicLong(0L);
        this.requestCount = new AtomicInteger(0);
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTokensUsed() {
        return tokensUsed.get();
    }

    public int getRequestCount() {
        return requestCount.get();
    }

    public void addTokens(long tokens) {
        tokensUsed.addAndGet(tokens);
    }

    public void incrementRequestCount() {
        requestCount.incrementAndGet();
    }
}

