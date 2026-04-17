package com.example.proxy.dto.response;

public record QuotaStatusResponse(
        String plan,
        long tokensUsed,
        long tokensRemaining,
        String resetDate,
        int requestsThisMinute,
        int requestLimitPerMinute
) {
}

