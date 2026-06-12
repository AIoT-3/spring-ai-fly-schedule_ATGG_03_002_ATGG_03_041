package com.nhnacademy.flyschedule.dto.request;

import com.nhnacademy.flyschedule.dto.ModelType;
import jakarta.validation.constraints.NotBlank;

public record NaturalLanguageFlightSearchRequest(
        @NotBlank
        String message,

        ModelType modelType
) {
}
