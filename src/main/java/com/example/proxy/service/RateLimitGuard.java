package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.exception.RateLimitExceededException;
import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;

public class RateLimitGuard implements TextGenerationService {

    private final TextGenerationService next;
    private final UsageTracker usageTracker;

    public RateLimitGuard(TextGenerationService next, UsageTracker usageTracker, AppProperties appProperties) {
        this.next = next;
        this.usageTracker = usageTracker;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        boolean acquired = usageTracker.tryAcquireRequestSlot(request.userId());
        if (!acquired) {
            int retryAfterSeconds = usageTracker.getSecondsUntilMinuteReset();
            throw new RateLimitExceededException("Rate limit exceeded", retryAfterSeconds);
        }
        return next.generate(request);
    }
}

