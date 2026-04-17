package com.example.proxy.controller;

import com.example.proxy.dto.request.UpgradePlanRequest;
import com.example.proxy.dto.response.QuotaHistoryItemResponse;
import com.example.proxy.dto.response.QuotaStatusResponse;
import com.example.proxy.dto.response.UpgradePlanResponse;
import com.example.proxy.service.QuotaManagementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quota")
public class QuotaController {

    private final QuotaManagementService quotaManagementService;

    public QuotaController(QuotaManagementService quotaManagementService) {
        this.quotaManagementService = quotaManagementService;
    }

    @GetMapping("/status")
    public QuotaStatusResponse getStatus(@RequestParam String userId) {
        return quotaManagementService.getStatus(userId);
    }

    @GetMapping("/history")
    public List<QuotaHistoryItemResponse> getHistory(@RequestParam String userId) {
        return quotaManagementService.getHistory(userId);
    }

    @PostMapping("/upgrade")
    public UpgradePlanResponse upgrade(@Valid @RequestBody UpgradePlanRequest request) {
        return quotaManagementService.upgradePlan(request.userId(), request.targetPlan());
    }
}

