package com.example.proxy.service;

import com.example.proxy.dto.response.GenerateTextResponse;
import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;
import org.springframework.stereotype.Service;

@Service
public class TextGenerationOrchestrator {

    private final TextGenerationService textGenerationService;
    private final TokenEstimator tokenEstimator;

    public TextGenerationOrchestrator(TextGenerationService textGenerationService, TokenEstimator tokenEstimator) {
        this.textGenerationService = textGenerationService;
        this.tokenEstimator = tokenEstimator;
    }

    public GenerateTextResponse generate(String userId, String prompt) {
        int requestedTokens = tokenEstimator.estimate(prompt);
        GenerationRequest request = new GenerationRequest(userId, prompt, requestedTokens);
        GenerationResponse response = textGenerationService.generate(request);
        return new GenerateTextResponse(response.text(), response.tokensUsed(), response.processingTimeMs());
    }
}

