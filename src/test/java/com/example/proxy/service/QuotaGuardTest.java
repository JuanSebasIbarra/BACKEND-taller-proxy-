package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.exception.QuotaExhaustedException;
import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;
import com.example.proxy.repository.InMemoryUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuotaGuardTest {

    private QuotaGuard quotaGuard;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = new AppProperties();
        UsageTracker usageTracker = new UsageTracker(new InMemoryUsageRepository(), appProperties);
        TextGenerationService next = request -> new GenerationResponse("ok", request.requestedTokens(), 1L);
        quotaGuard = new QuotaGuard(next, usageTracker, appProperties);
    }

    @Test
    void shouldThrowWhenQuotaIsExceeded() {
        GenerationRequest request = new GenerationRequest("quota-user", "hello", 50_001);

        assertThrows(QuotaExhaustedException.class, () -> quotaGuard.generate(request));
    }

    @Test
    void shouldDelegateWhenQuotaAvailable() {
        GenerationRequest request = new GenerationRequest("quota-ok", "hello", 100);
        GenerationResponse response = quotaGuard.generate(request);

        assertEquals("ok", response.text());
    }
}

