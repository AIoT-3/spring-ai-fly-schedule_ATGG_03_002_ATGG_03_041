package com.nhnacademy.flyschedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 정규화와 검증이 끝난 항공편 검색 조건 DTO
 *
 * @param departure 정규화된 출발 공항 이름
 * @param arrival 정규화된 도착 공항 이름
 * @param date DateParserAgent로 검증 및 변환된 검색 날짜
 * @param afterTime 출발 시간 하한. 조건이 없으면 null
 * @param beforeTime 출발 시간 상한. 조건이 없으면 null
 * @param minPrice 최소 일반석 운임. 조건이 없으면 null
 * @param maxPrice 최대 일반석 운임. 조건이 없으면 null
 * @param limitPerAirline 항공사별 반환할 최대 항공편 수
 */
public record FlightSearchCriteria(
        String departure,
        String arrival,
        LocalDate date,
        LocalTime afterTime,
        LocalTime beforeTime,
        Integer minPrice,
        Integer maxPrice,
        int limitPerAirline
) {
}
