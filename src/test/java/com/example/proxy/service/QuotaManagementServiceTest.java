package com.example.proxy.service;

import com.example.proxy.config.AppProperties;
import com.example.proxy.dto.response.UpgradePlanResponse;
import com.example.proxy.repository.InMemoryUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuotaManagementServiceTest {

    private QuotaManagementService quotaManagementService;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = new AppProperties();
        UsageTracker usageTracker = new UsageTracker(new InMemoryUsageRepository(), appProperties);
        quotaManagementService = new QuotaManagementService(usageTracker);
    }

    @Test
    void shouldUpgradeOnlyToPro() {
        UpgradePlanResponse response = quotaManagementService.upgradePlan("user-upgrade", "PRO");
        assertEquals("PRO", response.plan());

        assertThrows(IllegalArgumentException.class,
                () -> quotaManagementService.upgradePlan("user-upgrade", "ENTERPRISE"));
    }
}

