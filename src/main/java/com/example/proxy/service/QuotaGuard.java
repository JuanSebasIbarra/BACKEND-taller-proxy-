package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.exception.QuotaExhaustedException;
import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;

public class QuotaGuard implements TextGenerationService {

    private final TextGenerationService next;
    private final UsageTracker usageTracker;
    private final AppProperties appProperties;

    public QuotaGuard(TextGenerationService next, UsageTracker usageTracker, AppProperties appProperties) {
        this.next = next;
        this.usageTracker = usageTracker;
        this.appProperties = appProperties;
    }

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        boolean consumed = usageTracker.tryConsumeMonthlyTokens(request.userId(), request.requestedTokens());
        if (!consumed) {
            throw new QuotaExhaustedException("Monthly quota exhausted", appProperties.getUpgradeUrl());
        }
        return next.generate(request);
    }
}

