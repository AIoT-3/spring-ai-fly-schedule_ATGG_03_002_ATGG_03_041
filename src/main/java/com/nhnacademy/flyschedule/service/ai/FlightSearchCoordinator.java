package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.dto.ModelType;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightSearchAgent;
import com.nhnacademy.flyschedule.service.agent.PriceFilterAgent;
import com.nhnacademy.flyschedule.service.agent.TimeFilterAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchCoordinator {
    private static final int DEFAULT_LIMIT_PER_AIRLINE = 3;

    private final FlightSearchExtractService flightSearchExtractService;
    private final FlightSearchAgent flightSearchAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;

    public Map<String, List<FlightInfoResponse>> search(
            String message,
            ModelType modelType
    ) {
        validateText(message, "항공편 검색 문장을 입력해주세요.");
        modelType = ModelType.defaultIfNull(modelType);

        // 1. LLM으로 파라미터 추출
        FlightSearchExtractResult condition =
                flightSearchExtractService.extractFlightSearch(
                        message,
                        modelType
                );

        // 2. 항공편 검색
        Map<String, List<FlightInfoResponse>> flightsByAirline =
                flightSearchAgent.search(
                        condition.departure(),
                        condition.arrival(),
                        condition.date()
                );

        // 3. 필터링 후 반환
        return applyFilters(flightsByAirline, condition, normalizeLimit(DEFAULT_LIMIT_PER_AIRLINE));
    }

    private Map<String, List<FlightInfoResponse>> applyFilters(
            Map<String, List<FlightInfoResponse>> flightsByAirline,
            FlightSearchExtractResult condition,
            int limitPerAirline
    ) {
        if (flightsByAirline == null || flightsByAirline.isEmpty()) {
            return Map.of();
        }

        return flightsByAirline.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> applyFilters(entry.getValue(), condition, limitPerAirline),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    private List<FlightInfoResponse> applyFilters(
            List<FlightInfoResponse> flights,
            FlightSearchExtractResult condition,
            int limitPerAirline
    ) {
        List<FlightInfoResponse> filtered = flights == null ? List.of() : flights;

        if (condition.afterTime() != null) {
            filtered = timeFilterAgent.filterAfterTime(
                    filtered,
                    LocalTime.parse(condition.afterTime())
            );
        }

        if (condition.beforeTime() != null) {
            filtered = timeFilterAgent.filterBeforeTime(
                    filtered,
                    LocalTime.parse(condition.beforeTime())
            );
        }

        if (condition.minPrice() != null || condition.maxPrice() != null) {
            filtered = priceFilterAgent.filterByPriceRange(
                    filtered,
                    condition.minPrice(),
                    condition.maxPrice()
            );
        }

        return limitFlights(filtered, limitPerAirline);
    }

    private List<FlightInfoResponse> limitFlights(
            List<FlightInfoResponse> flights,
            int limit
    ) {
        if (flights == null || flights.isEmpty()) {
            return List.of();
        }

        return flights.stream()
                .limit(limit)
                .toList();
    }

    private int normalizeLimit(Integer limitPerAirline) {
        if (limitPerAirline == null || limitPerAirline <= 0) {
            return DEFAULT_LIMIT_PER_AIRLINE;
        }

        return limitPerAirline;
    }

    private void validateText(String text, String exceptionMessage) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
