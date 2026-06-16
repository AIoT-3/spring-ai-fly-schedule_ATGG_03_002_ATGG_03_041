package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 항공편 검색 원시 명령을 내부 검색 조건으로 정리하는 Agent입니다.
 *
 * <p>Coordinator 방식과 MCP Tool Orchestration이 서로 흐름을 공유하는
 * normalize, validate, type conversion 책임을 담당합니다.</p>
 *
 * <p>구현 예정 책임:</p>
 * <ul>
 *     <li>공항명, 날짜, 시간 문자열의 공백 제거</li>
 *     <li>{@code "null"} 문자열을 실제 null로 정리</li>
 *     <li>의미 없는 시간 sentinel 값 제거</li>
 *     <li>min, max 역전 보정</li>
 *     <li>필수 입력값 검증</li>
 *     <li>{@link DateParserAgent}를 사용한 날짜 검증 및 변환</li>
 *     <li>{@link FlightSearchCommand}를 {@link FlightSearchCriteria}로 변환</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class FlightSearchConditionAgent {
    private static final int DEFAULT_LIMIT_PER_AIRLINE = 3;
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final DateParserAgent dateParserAgent;

    /**
     * 원시 검색 명령을 정규화하고 검증한 뒤 내부 검색 조건으로 변환합니다.
     *
     * @param command Coordinator 또는 Tool에서 전달한 원시 검색 명령
     * @return 정규화와 검증이 완료된 검색 조건
     */
    public FlightSearchCriteria normalizeAndValidate(FlightSearchCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("항공편 검색 조건이 비어 있습니다.");
        }

        String departure = requireText(command.departure(), "출발 공항 이름은 필수입니다.");
        String arrival = requireText(command.arrival(), "도착 공항 이름은 필수입니다.");
        LocalDate date = validateDate(command.date());
        LocalTime afterTime = normalizeAfterTime(command.afterTime());
        LocalTime beforeTime = normalizeBeforeTime(command.beforeTime());
        PriceRange priceRange = normalizePriceRange(command.minPrice(), command.maxPrice());

        return new FlightSearchCriteria(
                departure,
                arrival,
                date,
                afterTime,
                beforeTime,
                priceRange.minPrice(),
                priceRange.maxPrice(),
                DEFAULT_LIMIT_PER_AIRLINE
        );
    }

    /**
     * 날짜 문자열이 검색 가능한 값인지 검증하고 내부 날짜 타입으로 변환합니다.
     *
     * <p>상대 날짜와 구체 날짜 처리는 {@link DateParserAgent}의 정책을 따릅니다.</p>
     *
     * @param date 원시 날짜 입력
     * @return 검색 기준 날짜
     */
    private LocalDate validateDate(String date) {
        String normalizedDate = requireText(date, "검색 날짜는 필수입니다.");

        try {
            // 기존에 구현한 parseDate를 사용함
            String apiDate = dateParserAgent.parseDate(normalizedDate);
            return LocalDate.parse(apiDate, API_DATE_FORMAT);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.", e);
        }
    }

    /**
     * 시간 문자열을 내부 시간 타입으로 변환할 수 있는지 검증합니다.
     *
     * @param time 원시 시간 입력
     * @return 검색 조건 시간. 조건이 없으면 null
     */
    private LocalTime validateTime(String time) {
        String normalizedTime = normalizeText(time);
        if (normalizedTime == null) {
            return null;
        }

        try {
            return LocalTime.parse(normalizedTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시간 형식은 HH:mm이어야 합니다.", e);
        }
    }

    /**
     * 최소/최대 가격 조건을 정리합니다.
     *
     * @param minPrice 원시 최소 가격
     * @param maxPrice 원시 최대 가격
     * @return 정리된 가격 범위
     */
    private PriceRange normalizePriceRange(Integer minPrice, Integer maxPrice) {
        Integer normalizedMinPrice = normalizeOptionalPrice(minPrice);
        Integer normalizedMaxPrice = normalizeOptionalPrice(maxPrice);

        if (normalizedMinPrice != null
                && normalizedMaxPrice != null
                && normalizedMinPrice > normalizedMaxPrice) {
            return new PriceRange(normalizedMaxPrice, normalizedMinPrice);
        }

        return new PriceRange(normalizedMinPrice, normalizedMaxPrice);
    }

    private LocalTime normalizeAfterTime(String afterTime) {
        LocalTime parsedAfterTime = validateTime(afterTime);
        if (LocalTime.MIDNIGHT.equals(parsedAfterTime)) {
            return null;
        }

        return parsedAfterTime;
    }

    private LocalTime normalizeBeforeTime(String beforeTime) {
        LocalTime parsedBeforeTime = validateTime(beforeTime);
        if (LocalTime.of(23, 59).equals(parsedBeforeTime)) {
            return null;
        }

        return parsedBeforeTime;
    }

    private String requireText(String value, String message) {
        String normalizedValue = normalizeText(value);
        if (normalizedValue == null) {
            throw new IllegalArgumentException(message);
        }

        return normalizedValue;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty() || "null".equalsIgnoreCase(trimmedValue)) {
            return null;
        }

        return trimmedValue;
    }

    private Integer normalizeOptionalPrice(Integer value) {
        if (value == null || value <= 0) {
            return null;
        }

        return value;
    }

    private record PriceRange(
            Integer minPrice,
            Integer maxPrice
    ) {
    }
}
