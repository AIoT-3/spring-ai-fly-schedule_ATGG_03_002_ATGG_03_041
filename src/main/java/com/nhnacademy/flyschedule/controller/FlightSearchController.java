package com.nhnacademy.flyschedule.controller;


import com.nhnacademy.flyschedule.dto.request.NaturalLanguageFlightSearchRequest;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.FlightSearchOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightSearchController {
    private final FlightSearchOrchestrator flightSearchOrchestrator;

    @PostMapping("/natural-language")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> searchByNaturalLanguage(
            @Valid @RequestBody NaturalLanguageFlightSearchRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flightSearchOrchestrator.search(
                        request.message(),
                        request.modelType()
                ));
    }
}
