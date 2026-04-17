package com.example.proxy.model;

public record GenerationRequest(String userId, String prompt, int requestedTokens) {
}

