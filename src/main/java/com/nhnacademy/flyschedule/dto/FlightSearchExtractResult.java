package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record FlightSearchExtractResult(
        @NotNull
        @JsonPropertyDescription("출발 공항 **이름** (예: 광주, 김포, 제주, 인천, 부산) - **반드시 한글 공항 이름만 사용**")
        String departure,

        @NotNull
        @JsonPropertyDescription("도착 공항 **이름** (예: 광주, 김포, 제주, 인천, 부산) - **반드시 한글 공항 이름만 사용**")
        String arrival,

        @NotNull
        @JsonPropertyDescription("날짜 (YYYY-MM-DD 형식, 예: 2026-03-25)")
        LocalDate date,

        @JsonPropertyDescription("기준 시간 (HH:MM 형식, 예: 10:00) - \"이후\" 조건만")
        LocalTime afterTime,

        @JsonPropertyDescription("기준 시간 (HH:MM 형식, 예: 18:00) - \"이전\" 조건만")
        LocalTime beforeTime,

        @JsonPropertyDescription("최소 가격")
        Integer minPrice,

        @JsonPropertyDescription("최대 가격")
        Integer maxPrice
) {
}
