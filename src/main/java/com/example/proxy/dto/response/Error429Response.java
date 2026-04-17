package com.example.proxy.dto.response;

public record Error429Response(String error, int retryAfterSeconds) {
}

