package com.nhnacademy.flyschedule.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.prompt.flight-search")
public class PromptProperties {
    private String systemPath;
    private String userPath;
}
