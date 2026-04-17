package com.example.proxy.service;

import com.example.proxy.model.GenerationRequest;
import com.example.proxy.model.GenerationResponse;

public interface TextGenerationService {
    GenerationResponse generate(GenerationRequest request);
}

