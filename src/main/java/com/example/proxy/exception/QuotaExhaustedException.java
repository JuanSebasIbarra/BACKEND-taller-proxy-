package com.example.proxy.exception;

public class QuotaExhaustedException extends RuntimeException {
    private final String upgradeUrl;

    public QuotaExhaustedException(String message, String upgradeUrl) {
        super(message);
        this.upgradeUrl = upgradeUrl;
    }

    public String getUpgradeUrl() {
        return upgradeUrl;
    }
}

