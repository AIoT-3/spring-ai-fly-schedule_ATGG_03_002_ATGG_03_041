package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.dto.ModelType;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchCoordinator {
    private final FlightSearchExtractService flightSearchExtractService;
    private final FlightSearchExecutionService flightSearchExecutionService;

    public Map<String, List<FlightInfoResponse>> search(
            String message,
            ModelType modelType
    ) {
        log.info("Coordinator flight search started: modelType={}, messageLength={}",
                modelType,
                message == null ? 0 : message.length());

        validateText(message, "항공편 검색 문장을 입력해주세요.");
        ModelType resolvedModelType = ModelType.defaultIfNull(modelType);
        log.info("Coordinator model type resolved: requested={}, resolved={}", modelType, resolvedModelType);

        // 1. LLM으로 파라미터 추출
        log.info("Coordinator extracting flight search condition");
        FlightSearchExtractResult condition =
                flightSearchExtractService.extractFlightSearch(
                        message,
                        resolvedModelType
                );
        log.info("Coordinator extraction completed: departure={}, arrival={}, date={}, afterTime={}, beforeTime={}, minPrice={}, maxPrice={}",
                condition.departure(),
                condition.arrival(),
                condition.date(),
                condition.afterTime(),
                condition.beforeTime(),
                condition.minPrice(),
                condition.maxPrice());

        // 2. 공통 항공편 검색 흐름 호출
        log.info("Coordinator converting extraction result to command");
        FlightSearchCommand command = FlightSearchCommand.fromExtractResult(condition);

        log.info("Coordinator executing common flight search pipeline");
        Map<String, List<FlightInfoResponse>> result = flightSearchExecutionService.search(command);
        log.info("Coordinator flight search completed: airlineCount={}, flightCount={}",
                result.size(),
                countFlights(result));

        return result;
    }

    private void validateText(String text, String exceptionMessage) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private int countFlights(Map<String, List<FlightInfoResponse>> flightsByAirline) {
        return flightsByAirline.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }
}
