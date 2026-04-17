package com.example.proxy.dto.response;

public record GenerateTextResponse(String text, int tokensUsed, long processingTimeMs) {
}

