package com.example.proxy.controller;

import com.example.proxy.dto.request.UpgradePlanRequest;
import com.example.proxy.dto.response.QuotaHistoryItemResponse;
import com.example.proxy.dto.response.QuotaStatusResponse;
import com.example.proxy.dto.response.UpgradePlanResponse;
import com.example.proxy.service.QuotaManagementService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuotaControllerTest {

    @Test
    void shouldReturnQuotaStatus() {
        QuotaManagementService service = new QuotaManagementService(null) {
            @Override
            public QuotaStatusResponse getStatus(String userId) {
                return new QuotaStatusResponse("FREE", 10, 49_990, "2026-05-01T00:00:00Z", 1, 10);
            }
        };

        QuotaController controller = new QuotaController(service);
        QuotaStatusResponse response = controller.getStatus("user-1");
        assertEquals("FREE", response.plan());
    }

    @Test
    void shouldUpgradePlan() {
        QuotaManagementService service = new QuotaManagementService(null) {
            @Override
            public UpgradePlanResponse upgradePlan(String userId, String targetPlan) {
                return new UpgradePlanResponse("PRO", "Plan upgraded successfully");
            }
        };

        QuotaController controller = new QuotaController(service);
        UpgradePlanResponse response = controller.upgrade(new UpgradePlanRequest("user-1", "PRO"));
        assertEquals("PRO", response.plan());
    }

    @Test
    void shouldReturnHistory() {
        QuotaManagementService service = new QuotaManagementService(null) {
            @Override
            public List<QuotaHistoryItemResponse> getHistory(String userId) {
                return List.of();
            }
        };

        QuotaController controller = new QuotaController(service);
        assertEquals(0, controller.getHistory("user-1").size());
    }
}


