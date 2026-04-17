package com.example.proxy.controller;

import com.example.proxy.dto.request.GenerateTextRequest;
import com.example.proxy.dto.response.GenerateTextResponse;
import com.example.proxy.service.TextGenerationOrchestrator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AiControllerTest {

    @Test
    void shouldReturnGeneratedText() {
        TextGenerationOrchestrator orchestrator = new TextGenerationOrchestrator(null, null) {
            @Override
            public GenerateTextResponse generate(String userId, String prompt) {
                return new GenerateTextResponse("mock text", 12, 1200L);
            }
        };

        AiController controller = new AiController(orchestrator);
        GenerateTextResponse response = controller.generate(new GenerateTextRequest("user-1", "hello"));

        assertEquals("mock text", response.text());
        assertEquals(12, response.tokensUsed());
    }
}


