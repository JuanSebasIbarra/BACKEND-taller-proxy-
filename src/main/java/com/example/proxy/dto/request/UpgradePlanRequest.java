package com.example.proxy.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpgradePlanRequest(
        @NotBlank String userId,
        @NotBlank String targetPlan
) {
}

