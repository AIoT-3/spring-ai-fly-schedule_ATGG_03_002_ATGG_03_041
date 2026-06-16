package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.ModelType;
import com.nhnacademy.flyschedule.dto.request.NaturalLanguageFlightSearchRequest;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.ai.FlightSearchA2A;
import com.nhnacademy.flyschedule.service.ai.FlightSearchCoordinator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/llm")
public class NaturalSearchController {
    private final FlightSearchCoordinator flightSearchCoordinator;
    private final FlightSearchA2A flightSearchA2A;

    @PostMapping("/search")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> searchByNaturalLanguage(
            @Valid @RequestBody NaturalLanguageFlightSearchRequest request,
            @RequestParam(value = "type", required = false, defaultValue = "default") String type
    ) {
        log.info("자연어 항공편 검색 요청 - 모드: {}, 모델: {}", type, request.modelType());

        Map<String, List<FlightInfoResponse>> result;

        if ("a2a".equalsIgnoreCase(type)) {
            result = flightSearchA2A.search(
                    request.message(),
                    ModelType.defaultIfNull(request.modelType())
            );
        } else {
            result = flightSearchCoordinator.search(
                    request.message(),
                    ModelType.defaultIfNull(request.modelType())
            );
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}