package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.FlightSearchA2A;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class A2AController {
    private final FlightSearchA2A flightSearchA2A;

    @GetMapping("/test")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> searchAirline(@RequestParam String text) {
        return ResponseEntity
                .ok(flightSearchA2A.search(text));
    }
}
