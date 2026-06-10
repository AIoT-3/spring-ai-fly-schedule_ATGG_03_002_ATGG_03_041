package com.nhnacademy.flyschedule.service.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class DateParserAgent {
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 사용자 입력 날짜를 API 요청 형식(yyyyMMdd)으로 변환합니다.
     *
     * <p>지원하는 입력 형식:</p>
     * <ul>
     *     <li>null 또는 빈 문자열 → 오늘 날짜</li>
     *     <li>yyyyMMdd 형식 → 그대로 반환</li>
     *     <li>"어제", "오늘", "내일", "모레" → 상대 날짜로 변환</li>
     *     <li>그 외 날짜 표현 → {@link #parseSpecificDate(String)}를 통해 변환</li>
     * </ul>
     *
     * @param dateInput 사용자가 입력한 날짜 문자열
     * @return API 요청에 사용할 yyyyMMdd 형식의 날짜 문자열
     */
    public String parseDate(String dateInput) {
        if (dateInput == null || dateInput.isEmpty()) {
            //따로 없는경우 오늘날자로 파싱
            return LocalDate.now().format(API_DATE_FORMAT);
        }

        String normalizedDate = dateInput.trim();

        // 정규화 된경우 바로 리턴
        if (normalizedDate.matches("\\d{8}")) {
            return normalizedDate;
        }

        return switch (normalizedDate) {
            case "어제" -> LocalDate.now().minusDays(1).format(API_DATE_FORMAT);
            case "오늘" -> LocalDate.now().format(API_DATE_FORMAT);
            case "내일" -> LocalDate.now().plusDays(1).format(API_DATE_FORMAT);
            case "모레" -> LocalDate.now().plusDays(2).format(API_DATE_FORMAT);

            default -> parseSpecificDate(normalizedDate); // 구체적인 날자가 주어진경우
        };

    }

    private String parseSpecificDate(String dateInput) {
        try {
            LocalDate date = LocalDate.parse(dateInput, INPUT_DATE_FORMAT);
            return date.format(API_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            log.error("잘못된 날짜형식입니다 : {}", dateInput);

            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다");
        }
    }
}
