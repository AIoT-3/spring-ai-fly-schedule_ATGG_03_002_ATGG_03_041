package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlightSearchExtractService {
    private final ChatClient chatClient;
    private final Validator validator;

    public FlightSearchExtractService(
            ChatClient.Builder chatClientBuilder,
            Validator validator
    ) {
        this.chatClient = chatClientBuilder.build();
        this.validator = validator;
    }

    public FlightSearchExtractResult extractFlightSearch(String message) {
        return extractFlightSearch(message, LocalDate.now().toString());
    }

    FlightSearchExtractResult extractFlightSearch(String message, String baseDate) {
        FlightSearchExtractResult result;

        try {
            result = chatClient.prompt()
                    .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                    .system("""
                            너는 항공편 검색 시스템의 파라미터 추출 전문가다.
                            자연어로 된 사용자 메시지를 분석하여 항공편 검색에 필요한 파라미터를 추출한다.
                            
                            ## 규칙
                            1. 날짜는 반드시 YYYY-MM-DD 형식으로 변환
                            2. 시간은 24시간제 HH:MM 형식
                            3. "내일", "모레" 등 상대적 날짜 표현은 오늘 날짜를 기준으로 계산하여 YYYY-MM-DD로 변환
                            4. "오후 3시", "14시" 등의 표현은 HH:MM으로 변환
                            5. 하나의 가격 값만 등장한 경우, 문맥에 따라 minPrice 또는 maxPrice 중 하나만 설정, 다른 값은 반드시 null로 설정.
                            6. **사용자가 말하지 않은 파라미터는 추론하지 않고 null로 반환**
                            """)
                    .user(u -> u.text("""
                                    오늘 날짜: {baseDate}
                                    
                                    ### 사용자 메세지
                                    {message}""")
                            .param("baseDate", baseDate)
                            .param("message", message)
                    ).call()
                    .entity(FlightSearchExtractResult.class);

            log.info("파라미터 추출 결과 : {}", result);
        } catch (Exception e) {
            log.warn("항공편 검색 파라미터 추출 실패: message={}", message, e);
            throw new IllegalArgumentException("항공편 검색 파라미터를 추출하지 못했습니다.", e);
        }

        result = normalize(result);
        validate(result);

        log.info("최종 결과값 : {}", result);

        return result;
    }

    private FlightSearchExtractResult normalize(FlightSearchExtractResult result) {
        if (result == null) {
            return null;
        }

        return new FlightSearchExtractResult(
                result.departure(),
                result.arrival(),
                result.date(),
                normalizeText(result.afterTime()),
                normalizeText(result.beforeTime()),
                normalizeOptionalPrice(result.minPrice()),
                normalizeOptionalPrice(result.maxPrice())
        );
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

    private void validate(FlightSearchExtractResult result) {
        if (result == null) {
            throw new IllegalArgumentException("항공편 검색 파라미터 추출 결과가 비어 있습니다.");
        }

        Set<ConstraintViolation<FlightSearchExtractResult>> violations =
                validator.validate(result);

        if (!violations.isEmpty()) {
            String violationMessage = violations.stream()
                    .map(violation -> "%s: %s".formatted(
                            violation.getPropertyPath(),
                            violation.getMessage()
                    ))
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException("항공편 검색 필수 파라미터가 누락되었습니다: " + violationMessage);
        }
    }
}
