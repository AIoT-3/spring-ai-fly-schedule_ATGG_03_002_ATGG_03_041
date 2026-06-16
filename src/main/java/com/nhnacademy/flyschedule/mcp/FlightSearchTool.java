package com.nhnacademy.flyschedule.mcp;

import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.ai.FlightSearchOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchTool {
    private final FlightSearchOrchestrator flightSearchOrchestrator;

    @Tool(
            name = "search_flights_with_filters",
            description = """
                    자연어 항공편 검색 요청을 처리하는 단일 통합 도구입니다.
                    출발 공항명, 도착 공항명, 날짜를 기준으로 항공편을 조회하고,
                    선택적으로 출발 시간 이후/이전 조건과 일반석 운임 범위를 함께 적용합니다.
                    날짜는 '내일', '모레', '2026-06-15' 같은 표현을 전달할 수 있습니다.
                    시간 조건은 HH:mm 형식으로 전달합니다. 예: 13:00, 18:30
                    가격 조건은 원 단위 정수로 전달합니다. 예: 50000
                    """,
            returnDirect = true

    )
    public Map<String, List<FlightInfoResponse>> searchFlightsWithFilters(
            @ToolParam(description = "출발 공항 이름. 예: 광주, 김포, 제주") String departure,
            @ToolParam(description = "도착 공항 이름. 예: 제주, 김포, 부산") String arrival,
            @ToolParam(description = "검색 날짜. 예: 내일, 모레, 2026-06-15") String date,
            @ToolParam(required = false, description = "이 시간 이후 출발 항공편만 조회합니다. HH:mm 형식. 예: 13:00") String afterTime,
            @ToolParam(required = false, description = "이 시간 이전 출발 항공편만 조회합니다. HH:mm 형식. 예: 18:00") String beforeTime,
            @ToolParam(required = false, description = "최소 일반석 운임. 지정하지 않으면 하한 없음") Integer minPrice,
            @ToolParam(required = false, description = "최대 일반석 운임. 지정하지 않으면 상한 없음") Integer maxPrice
    ) {
        return flightSearchOrchestrator.searchFlightsWithFilters(
                departure,
                arrival,
                date,
                afterTime,
                beforeTime,
                minPrice,
                maxPrice
        );
    }
}
