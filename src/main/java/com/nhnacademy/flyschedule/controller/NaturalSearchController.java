package com.nhnacademy.flyschedule.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.ai.FlightSearchExtractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/llm")
public class NaturalSearchController {
    private final FlightSearchExtractService flightSearchExtractService;


    @GetMapping("/search")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> getFlightSearch(@RequestParam String message) {
        FlightSearchExtractResult extractResult = flightSearchExtractService.extractFlightSearch(message);

        log.info("Extract result: {}", extractResult);

        return ResponseEntity.ok()
                .body(Map.of());

    }
}
