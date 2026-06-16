package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.response.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mcp")
public class McpController {
    private final AirportInfoTool airportInfoTool;
    private final AirlineInfoTool airlineInfoTool;
    private final FlightSearchTool flightSearchTool;

    /**
     * 공항 목록 조회
     *
     * @return 공항 정보 목록
     */
    @GetMapping("/airports")
    public ResponseEntity<List<AirportInfoResponse>> getAirports() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(airportInfoTool.getAirportInfo());
    }

    /**
     * 공항 코드 조회
     *
     * @param airportName 공항 이름
     * @return 공항 정보
     */
    @GetMapping("/airports/code")
    public ResponseEntity<AirportInfoResponse> getAirportInfo(@RequestParam String airportName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AirportInfoResponse(airportName, airportInfoTool.getAirportCode(airportName)));
    }


    /**
     * 항공사 목록 조회
     *
     * @return 항공사 정보 목록
     */
    @GetMapping("/airlines")
    public ResponseEntity<List<AirlineInfoResponse>> getAirlines() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(airlineInfoTool.getAirlineInfo());
    }


    /**
     * 항공사 ID 조회
     *
     * @param airlineName 항공사 이름
     * @return 항공사
     */
    @GetMapping("/airlines/id")
    public ResponseEntity<AirlineInfoResponse> getAirline(@RequestParam String airlineName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AirlineInfoResponse(airlineName, airlineInfoTool.getAirlineId(airlineName)));
    }

    /**
     * 항공편 검색 (항공사별 그룹핑)
     *
     * @param departure 출발 공항 이름
     * @param arrival   도착 공항 이름
     * @param date      날짜
     * @param afterTime 출발 시간 하한
     * @param beforeTime 출발 시간 상한
     * @param minPrice 최소 가격
     * @param maxPrice 최대 가격
     * @return 항공사별로 그룹핑된 항공편
     */
    @GetMapping("/flight/search")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> getFlightSearch(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date,
            @RequestParam(required = false) String afterTime,
            @RequestParam(required = false) String beforeTime,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flightSearchTool.searchFlightsWithFilters(
                        departure,
                        arrival,
                        date,
                        afterTime,
                        beforeTime,
                        minPrice,
                        maxPrice
                ));
    }

    /**
     * 시간 필터 항공편 검색
     *
     * @param departure 출발 공항 이름
     * @param arrival   도착 공항 이름
     * @param date      날짜
     * @param afterTime 기준 시간
     * @return 항공사별로 그룹핑된 항공편
     */
    @GetMapping("/flight/search/time")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> getFlightSearchWithTimeFilter(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date,
            @RequestParam String afterTime
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flightSearchTool.searchFlightsWithFilters(
                        departure,
                        arrival,
                        date,
                        afterTime,
                        null,
                        null,
                        null
                ));
    }

    /**
     * 가격 필터 항공편 검색
     *
     * @param departure 출발 공항 이름
     * @param arrival   도착 공항 이름
     * @param date      날짜
     * @param minPrice  최소 가격
     * @param maxPrice  최대 가격
     * @return 항공사별로 그룹핑된 항공편
     */
    @GetMapping("/flight/search/price")
    public ResponseEntity<Map<String, List<FlightInfoResponse>>> getFlightSearchWithPriceFilter(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String date,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(flightSearchTool.searchFlightsWithFilters(
                        departure,
                        arrival,
                        date,
                        null,
                        null,
                        minPrice,
                        maxPrice
                ));
    }

}
