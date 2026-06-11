package com.nhnacademy.flyschedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record FlightSearchExtractResult(
        @NotBlank
        String departure,

        @NotBlank
        String arrival,

        @NotBlank
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
        String date,

        @Pattern(regexp = "([01]\\d|2[0-3]):[0-5]\\d")
        String afterTime,

        @Pattern(regexp = "([01]\\d|2[0-3]):[0-5]\\d")
        String beforeTime,

        @Positive
        Integer minPrice,

        @Positive
        Integer maxPrice
) {
}
