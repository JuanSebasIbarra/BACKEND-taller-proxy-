package com.example.proxy.controller;

import com.example.proxy.dto.request.GenerateTextRequest;
import com.example.proxy.dto.response.GenerateTextResponse;
import com.example.proxy.service.TextGenerationOrchestrator;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final TextGenerationOrchestrator orchestrator;

    public AiController(TextGenerationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/generate")
    public GenerateTextResponse generate(@Valid @RequestBody GenerateTextRequest request) {
        return orchestrator.generate(request.userId(), request.prompt());
    }
}

