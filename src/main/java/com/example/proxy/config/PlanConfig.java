package com.example.proxy.config;

import com.example.proxy.model.PlanLimits;
import com.example.proxy.model.PlanType;
import org.springframework.stereotype.Component;

@Component
public class PlanConfig {

    private final AppProperties appProperties;

    public PlanConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public PlanLimits limitsFor(PlanType planType) {
        return appProperties.getPlanLimits(planType);
    }
}

