package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.mcp.AirlineInfoTool;
import com.nhnacademy.flyschedule.mcp.AirportInfoTool;
import com.nhnacademy.flyschedule.mcp.FlightSearchTool;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mcp")
public class McpController {
    private final AirportInfoTool airportInfoTool;
    private final AirlineInfoTool airlineInfoTool;
    private final FlightSearchTool flightSearchTool;

    @GetMapping("/airports")
    public ResponseEntity<List<AirportInfoResponse>> getAirports() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(airportInfoTool.getAirportInfo());
    }

    @GetMapping("/airports/code")
    public ResponseEntity<AirportInfoResponse> getAirportInfo(@RequestParam String airportName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AirportInfoResponse(airportName, airportInfoTool.getAirportCode(airportName)));
    }

    @GetMapping("/airlines")
    public ResponseEntity<List<AirlineInfoResponse>> getAirlines() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(airlineInfoTool.getAirlineInfo());
    }

    @GetMapping("/airlines/id")
    public ResponseEntity<AirlineInfoResponse> getAirline(@RequestParam String airlineName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AirlineInfoResponse(airlineName, airlineInfoTool.getAirlineId(airlineName)));
    }

    @GetMapping("/flight/search")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> getFlightSearch(@Valid FlightInfoRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flightSearchTool.searchFlights(request.depAirportId(), request.arrAirportId(), request.departmentDate(), 3));
    }

}
