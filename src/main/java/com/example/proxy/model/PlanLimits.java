package com.example.proxy.model;

public record PlanLimits(int requestsPerMinute, long monthlyTokens, boolean unlimited) {
}

