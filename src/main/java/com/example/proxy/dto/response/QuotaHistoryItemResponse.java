package com.example.proxy.dto.response;

public record QuotaHistoryItemResponse(String date, long tokensUsed, int requestCount) {
}

