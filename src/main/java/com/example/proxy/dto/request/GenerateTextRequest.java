package com.example.proxy.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GenerateTextRequest(
        @NotBlank String userId,
        @NotBlank String prompt
) {
}

