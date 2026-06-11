package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.service.ai.prompt.FlightSearchPrompt;
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
    private final FlightSearchPrompt flightSearchPrompt;

    public FlightSearchExtractService(
            ChatClient.Builder chatClientBuilder,
            Validator validator,
            FlightSearchPrompt flightSearchPrompt
    ) {
        this.chatClient = chatClientBuilder.build();
        this.validator = validator;
        this.flightSearchPrompt = flightSearchPrompt;
    }

    public FlightSearchExtractResult extractFlightSearch(String message) {
        return extractFlightSearch(message, LocalDate.now().toString());
    }

    FlightSearchExtractResult extractFlightSearch(String message, String baseDate) {
        FlightSearchExtractResult result;

        try {
            result = chatClient.prompt()
                    .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                    .system(flightSearchPrompt.system())
                    .user(u -> u.text(flightSearchPrompt.user())
                            .param("baseDate", baseDate)
                            .param("prompt", message)
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

        PriceRange priceRange = normalizePriceRange(result.minPrice(), result.maxPrice());

        return new FlightSearchExtractResult(
                normalizeText(result.departure()),
                normalizeText(result.arrival()),
                normalizeText(result.date()),
                normalizeText(result.afterTime()),
                normalizeText(result.beforeTime()),
                priceRange.minPrice(),
                priceRange.maxPrice()
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

    private record PriceRange(
            Integer minPrice,
            Integer maxPrice
    ) {
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
