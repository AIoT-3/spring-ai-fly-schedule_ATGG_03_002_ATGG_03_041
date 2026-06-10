package com.nhnacademy.flyschedule.mcp;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightSearchAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchTool {
    private static final int DEFAULT_LIMIT_PER_AIRLINE = 3;

    private final FlightSearchAgent flightSearchAgent;

    @Tool(
            description = """
                    항공편을 검색하여 항공사별로 그룹핑하여 반환합니다.
                    출발 공항, 도착 공항, 날짜를 받아 항공사별로 정리된 항공편 목록을 제공합니다.
                    날짜는 '내일', '모레', '2025-03-09' 형식을 지원합니다.
                    빠른 응답을 위해 항공사별 지정된 개수만 반환하며, 생략하면 최대 3편만 반환합니다.
                    """
    )
    public Map<String, List<FlightInfoResponse>> searchFlights(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 김포, 제주)") String departure,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 김포, 부산)") String arrival,
            @ToolParam(description = "날짜 (예: 내일, 모레, 2025-03-09)") String date,
            @ToolParam(required = false, description = "항공사별 반환할 최대 항공편 수. 생략하면 3") Integer limitPerAirline
    ) {
        log.info("MCP Tool 호출: flights(departure={}, arrival={}, date={})",
                departure, arrival, date);

        return limitFlightsPerAirline(
                flightSearchAgent.search(departure, arrival, date),
                limitPerAirline
        );
    }

    @Tool(
            description = """
                    항공편을 검색한 뒤 특정 시간 이후에 출발하는 항공편만 항공사별로 그룹핑하여 반환합니다.
                    시간은 HH:mm 형식으로 입력합니다. 예: 13:00
                    빠른 응답을 위해 항공사별 지정된 개수만 반환하며, 생략하면 최대 3편만 반환합니다.
                    """
    )
    public Map<String, List<FlightInfoResponse>> searchFlightsWithTimeFilter(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 김포, 제주)") String departure,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 김포, 부산)") String arrival,
            @ToolParam(description = "날짜 (예: 내일, 모레, 2025-03-09)") String date,
            @ToolParam(description = "이 시간 이후 출발 항공편만 조회합니다. HH:mm 형식 (예: 13:00)") String afterTime,
            @ToolParam(required = false, description = "항공사별 반환할 최대 항공편 수. 생략하면 3") Integer limitPerAirline
    ) {
        log.info("MCP Tool 호출: flightsTimeFilter(departure={}, arrival={}, date={}, afterTime={})",
                departure, arrival, date, afterTime);

        return limitFlightsPerAirline(
                flightSearchAgent.searchWithTimeFilter(departure, arrival, date, afterTime),
                limitPerAirline
        );
    }

    @Tool(
            description = """
                    항공편을 검색한 뒤 일반석 운임 범위에 포함되는 항공편만 항공사별로 그룹핑하여 반환합니다.
                    최소/최대 가격 중 하나만 지정할 수 있습니다.
                    빠른 응답을 위해 항공사별 지정된 개수만 반환하며, 생략하면 최대 3편만 반환합니다.
                    """
    )
    public Map<String, List<FlightInfoResponse>> searchFlightsWithPriceFilter(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 김포, 제주)") String departure,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 김포, 부산)") String arrival,
            @ToolParam(description = "날짜 (예: 내일, 모레, 2025-03-09)") String date,
            @ToolParam(required = false, description = "최소 일반석 운임. 지정하지 않으면 하한 없음") String minPrice,
            @ToolParam(required = false, description = "최대 일반석 운임. 지정하지 않으면 상한 없음") String maxPrice,
            @ToolParam(required = false, description = "항공사별 반환할 최대 항공편 수. 생략하면 3") Integer limitPerAirline
    ) {
        log.info("MCP Tool 호출: flightsPriceFilter(departure={}, arrival={}, date={}, minPrice={}, maxPrice={})",
                departure, arrival, date, minPrice, maxPrice);

        return limitFlightsPerAirline(
                flightSearchAgent.searchWithPriceFilter(departure, arrival, date, minPrice, maxPrice),
                limitPerAirline
        );
    }

    private Map<String, List<FlightInfoResponse>> limitFlightsPerAirline(
            Map<String, List<FlightInfoResponse>> flightsByAirline,
            Integer limitPerAirline
    ) {
        if (flightsByAirline == null || flightsByAirline.isEmpty()) {
            return Map.of();
        }

        int limit = normalizeLimit(limitPerAirline);

        return flightsByAirline.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> limitFlights(entry.getValue(), limit),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
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
}
