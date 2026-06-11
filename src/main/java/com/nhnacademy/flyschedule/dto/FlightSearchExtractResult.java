package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import jakarta.validation.constraints.NotNull;

public record FlightSearchExtractResult(
        @NotNull
        @JsonPropertyDescription("**출발** 공항 이름 (예: 광주, 김포, 제주, 인천, 부산) - **반드시 한글 공항 이름만 사용**")
        String departure,

        @NotNull
        @JsonPropertyDescription("**도착** 공항 이름 (예: 광주, 김포, 제주, 인천, 부산) - **반드시 한글 공항 이름만 사용**")
        String arrival,

        @NotNull
        @JsonPropertyDescription("날짜 (YYYY-MM-DD 형식, 예: 2026-03-25)")
        String date,

        @JsonPropertyDescription("기준 시간 (24시간제 HH:MM 형식, 예: 10:00) - \"이후\" 조건만")
        String afterTime,

        @JsonPropertyDescription("기준 시간 (24시간제 HH:MM 형식, 예: 18:00) - \"이전\" 조건만")
        String beforeTime,

        @JsonPropertyDescription("**최소** 가격 - (예: 0만원부터, 최소 0만원, 0만원 이상)")
        Integer minPrice,

        @JsonPropertyDescription("**최대** 가격 - (예: 0만원 밑, 0만원 아래, 최대 0만원)")
        Integer maxPrice
) {
}
