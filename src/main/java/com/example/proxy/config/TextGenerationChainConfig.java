package com.example.proxy.config;

import com.example.proxy.service.MockTextGenerationService;
import com.example.proxy.service.QuotaGuard;
import com.example.proxy.service.RateLimitGuard;
import com.example.proxy.service.TextGenerationService;
import com.example.proxy.service.UsageTracker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextGenerationChainConfig {

    @Bean
    public TextGenerationService textGenerationService(UsageTracker usageTracker, AppProperties appProperties) {
        TextGenerationService coreService = new MockTextGenerationService();
        TextGenerationService quotaGuard = new QuotaGuard(coreService, usageTracker, appProperties);
        return new RateLimitGuard(quotaGuard, usageTracker, appProperties);
    }
}

