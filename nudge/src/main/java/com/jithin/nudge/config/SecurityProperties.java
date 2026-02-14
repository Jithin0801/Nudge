package com.jithin.nudge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nudge.security")
public record SecurityProperties(String keystorePath, String keystorePassword, String keyAlias, String keyPassword, String allowedOrigins) {
}
