package com.example.proxy.service;

import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MockTextGenerationService implements TextGenerationService {

    private static final List<String> MOCK_TEXTS = List.of(
            "Here is a concise AI generated response.",
            "This mock answer simulates generated content.",
            "Generated text from the simulated AI engine.",
            "Your prompt was processed successfully by the mock provider."
    );

    @Override
    public GenerationResponse generate(GenerationRequest request) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(1_200L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Text generation was interrupted", ex);
        }

        String text = MOCK_TEXTS.get(ThreadLocalRandom.current().nextInt(MOCK_TEXTS.size()));
        long processingTime = System.currentTimeMillis() - start;
        return new GenerationResponse(text, request.requestedTokens(), processingTime);
    }
}

