package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.mcp.AirportInfoTool;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mcp")
public class McpController {
    private final AirportInfoTool airportInfoTool;

    @GetMapping("/airports")
    public ResponseEntity<List<AirportInfoResponse>> getAirports() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(airportInfoTool.getAirportInfo());
    }
}
