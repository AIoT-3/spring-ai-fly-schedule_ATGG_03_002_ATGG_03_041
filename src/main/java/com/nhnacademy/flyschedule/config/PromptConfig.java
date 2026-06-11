package com.nhnacademy.flyschedule.config;

import com.nhnacademy.flyschedule.service.ai.prompt.FlightSearchPrompt;
import com.nhnacademy.flyschedule.service.ai.prompt.PromptResourceLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PromptProperties.class)
public class PromptConfig {

    @Bean
    public FlightSearchPrompt flightSearchPrompt(
            PromptProperties promptProperties,
            PromptResourceLoader promptResourceLoader
    ) {
        return new FlightSearchPrompt(
                promptResourceLoader.load(promptProperties.getSystemPath()),
                promptResourceLoader.load(promptProperties.getUserPath())
        );
    }
}
