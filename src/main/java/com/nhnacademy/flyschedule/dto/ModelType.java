package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ModelType {
    OLLAMA,
    GEMINI;

    // TODO
    //  1. 환경설정에 따라 기본 모델을 설정할 수 있도록 변경
    //  2. ChatClientConfig의 Primary model도 설정 할 수 있도록 통합
    public static final ModelType defaultModel = ModelType.OLLAMA;

    public static ModelType defaultIfNull(ModelType mode) {
        return mode == null ? defaultModel : mode;
    }

    @JsonCreator
    public static ModelType from(String value) {
        if (value == null || value.isBlank()) {
            log.warn("Unknown ModelType value: {}", value);
            return defaultModel;
        }

        return ModelType.valueOf(value.trim().toUpperCase());
    }
}
