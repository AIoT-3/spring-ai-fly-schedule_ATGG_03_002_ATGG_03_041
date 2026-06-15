package com.nhnacademy.flyschedule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app.prompt.flight-search")
public record PromptProperties (
    String systemPath,
    String userPath
) {}
