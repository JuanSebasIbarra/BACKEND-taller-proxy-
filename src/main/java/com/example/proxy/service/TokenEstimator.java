package com.example.proxy.service;

import org.springframework.stereotype.Component;

@Component
public class TokenEstimator {

    public int estimate(String prompt) {
        int estimated = Math.max(1, prompt.length() / 4);
        return Math.min(estimated, 4_000);
    }
}

