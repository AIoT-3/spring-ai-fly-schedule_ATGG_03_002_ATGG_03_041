package com.nhnacademy.flyschedule.service.ai.prompt;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PromptResourceLoader {
    private static final String CLASSPATH_PREFIX = "classpath:";

    private final ResourceLoader resourceLoader;

    public PromptResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String path) {
        Assert.hasText(path, "prompt path cannot be null or empty");

        String resourcePath = path.startsWith(CLASSPATH_PREFIX) ? path : CLASSPATH_PREFIX + path;
        Resource resource = resourceLoader.getResource(resourcePath);

        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Prompt 파일을 읽지 못했습니다: " + path, e);
        }
    }
}
