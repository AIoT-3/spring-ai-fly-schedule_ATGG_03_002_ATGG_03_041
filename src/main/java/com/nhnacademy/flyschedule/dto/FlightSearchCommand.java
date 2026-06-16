package com.nhnacademy.flyschedule.dto;

/**
 * 항공편 검색 공통 실행 흐름에 전달되는 원시 검색 명령 DTO
 *
 * @param departure 출발 공항 이름 원시 입력
 * @param arrival 도착 공항 이름 원시 입력
 * @param date 검색 날짜 원시 입력. 예: 오늘, 내일, 모레, 2026-06-15
 * @param afterTime 출발 시간 하한 원시 입력. 예: 13:00
 * @param beforeTime 출발 시간 상한 원시 입력. 예: 18:00
 * @param minPrice 최소 일반석 운임 원시 입력
 * @param maxPrice 최대 일반석 운임 원시 입력
 */
public record FlightSearchCommand(
        String departure,
        String arrival,
        String date,
        String afterTime,
        String beforeTime,
        Integer minPrice,
        Integer maxPrice
) {
    /**
     * Coordinator 흐름에서 LLM 추출 결과를 공통 검색 명령으로 변환합니다.
     *
     * @param result LLM이 추출한 항공편 검색 조건
     * @return 공통 실행 흐름에 전달할 원시 검색 명령
     */
    public static FlightSearchCommand fromExtractResult(FlightSearchExtractResult result) {
        if (result == null) {
            throw new IllegalArgumentException("항공편 검색 추출 결과가 비어 있습니다.");
        }

        return new FlightSearchCommand(
                result.departure(),
                result.arrival(),
                result.date(),
                result.afterTime(),
                result.beforeTime(),
                result.minPrice(),
                result.maxPrice()
        );
    }

    /**
     * MCP Tool Orchestration 흐름에서 tool arguments를 공통 검색 명령으로 변환합니다.
     *
     * @param departure 출발 공항 이름 원시 입력
     * @param arrival 도착 공항 이름 원시 입력
     * @param date 검색 날짜 원시 입력
     * @param afterTime 출발 시간 하한 원시 입력
     * @param beforeTime 출발 시간 상한 원시 입력
     * @param minPrice 최소 일반석 운임 원시 입력
     * @param maxPrice 최대 일반석 운임 원시 입력
     * @return 공통 실행 흐름에 전달할 원시 검색 명령
     */
    public static FlightSearchCommand fromToolArguments(
            String departure,
            String arrival,
            String date,
            String afterTime,
            String beforeTime,
            Integer minPrice,
            Integer maxPrice
    ) {
        return new FlightSearchCommand(
                departure,
                arrival,
                date,
                afterTime,
                beforeTime,
                minPrice,
                maxPrice
        );
    }
}
