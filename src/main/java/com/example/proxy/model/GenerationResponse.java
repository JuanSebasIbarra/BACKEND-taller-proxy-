package com.example.proxy.model;

public record GenerationResponse(String text, int tokensUsed, long processingTimeMs) {
}

