package com.example.proxy.config;

import com.example.proxy.model.PlanLimits;
import com.example.proxy.model.PlanType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String upgradeUrl = "/pricing";
    private final Cors cors = new Cors();
    private final Plans plans = new Plans();

    public String getUpgradeUrl() {
        return upgradeUrl;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        this.upgradeUrl = upgradeUrl;
    }

    public Cors getCors() {
        return cors;
    }

    public Plans getPlans() {
        return plans;
    }

    public PlanLimits getPlanLimits(PlanType planType) {
        return switch (planType) {
            case FREE -> new PlanLimits(plans.free.requestsPerMinute, plans.free.monthlyTokens, plans.free.unlimited);
            case PRO -> new PlanLimits(plans.pro.requestsPerMinute, plans.pro.monthlyTokens, plans.pro.unlimited);
            case ENTERPRISE -> new PlanLimits(plans.enterprise.requestsPerMinute, plans.enterprise.monthlyTokens, plans.enterprise.unlimited);
        };
    }

    public static class Cors {
        private List<String> allowedOrigins = List.of("http://localhost:3000");

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Plans {
        private final PlanSetting free = new PlanSetting(10, 50_000L, false);
        private final PlanSetting pro = new PlanSetting(60, 500_000L, false);
        private final PlanSetting enterprise = new PlanSetting(-1, -1L, true);

        public PlanSetting getFree() {
            return free;
        }

        public PlanSetting getPro() {
            return pro;
        }

        public PlanSetting getEnterprise() {
            return enterprise;
        }
    }

    public static class PlanSetting {
        private int requestsPerMinute;
        private long monthlyTokens;
        private boolean unlimited;

        public PlanSetting() {
        }

        public PlanSetting(int requestsPerMinute, long monthlyTokens, boolean unlimited) {
            this.requestsPerMinute = requestsPerMinute;
            this.monthlyTokens = monthlyTokens;
            this.unlimited = unlimited;
        }

        public int getRequestsPerMinute() {
            return requestsPerMinute;
        }

        public void setRequestsPerMinute(int requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
        }

        public long getMonthlyTokens() {
            return monthlyTokens;
        }

        public void setMonthlyTokens(long monthlyTokens) {
            this.monthlyTokens = monthlyTokens;
        }

        public boolean isUnlimited() {
            return unlimited;
        }

        public void setUnlimited(boolean unlimited) {
            this.unlimited = unlimited;
        }
    }
}

