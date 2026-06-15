package com.nhnacademy.flyschedule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data-go-kr.api")
public record DataGoKrApiProperties (
    String url,
    String serviceKey
) {}