package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.exception.RateLimitExceededException;
import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;
import com.example.proxy.repository.InMemoryUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RateLimitGuardTest {

    private RateLimitGuard rateLimitGuard;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = new AppProperties();
        UsageTracker usageTracker = new UsageTracker(new InMemoryUsageRepository(), appProperties);
        TextGenerationService next = request -> new GenerationResponse("ok", request.requestedTokens(), 1L);
        rateLimitGuard = new RateLimitGuard(next, usageTracker, appProperties);
    }

    @Test
    void shouldThrowWhenRateLimitIsExceeded() {
        GenerationRequest request = new GenerationRequest("user-1", "hello", 10);

        for (int i = 0; i < 10; i++) {
            rateLimitGuard.generate(request);
        }

        assertThrows(RateLimitExceededException.class, () -> rateLimitGuard.generate(request));
    }

    @Test
    void shouldDelegateWhenUnderLimit() {
        GenerationRequest request = new GenerationRequest("user-2", "hello", 10);
        GenerationResponse response = rateLimitGuard.generate(request);

        assertEquals("ok", response.text());
        assertEquals(10, response.tokensUsed());
    }
}

