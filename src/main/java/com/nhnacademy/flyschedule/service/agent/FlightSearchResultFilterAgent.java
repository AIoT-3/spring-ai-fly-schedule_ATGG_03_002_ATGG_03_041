package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 항공편 검색 결과에 공통 후처리 정책을 적용하는 Agent입니다.
 *
 * <p>Coordinator 방식과 MCP Tool Orchestration 방식이 같은 검색 조건에 대해
 * 같은 결과를 반환하도록 시간, 가격, limit 필터링 책임을 이 Agent로 모읍니다.</p>
 *
 * <p>구현 책임:</p>
 * <ul>
 *     <li>{@link FlightSearchCriteria#afterTime()} 조건 적용</li>
 *     <li>{@link FlightSearchCriteria#beforeTime()} 조건 적용</li>
 *     <li>일반석 운임 범위 조건 적용</li>
 *     <li>필터링된 항공편 목록을 항공사별로 그룹핑</li>
 *     <li>항공사별 반환 개수 제한 적용</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class FlightSearchResultFilterAgent {
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final FlightGroupingAgent flightGroupingAgent;

    /**
     * 원본 항공편 검색 결과에 criteria 기반 필터를 적용하고 항공사별로 그룹핑합니다.
     *
     * @param flights 원본 항공편 검색 결과
     * @param criteria 정규화와 검증이 완료된 검색 조건
     * @return 필터링과 limit 적용이 끝난 항공사별 항공편 검색 결과
     */
    public Map<String, List<FlightInfoResponse>> apply(
            List<FlightInfoResponse> flights,
            FlightSearchCriteria criteria
    ) {
        if (criteria == null) {
            throw new IllegalArgumentException("항공편 검색 조건은 필수입니다.");
        }

        List<FlightInfoResponse> filtered = applyFilters(flights, criteria);
        if (filtered.isEmpty()) {
            return Map.of();
        }

        return flightGroupingAgent.groupByAirline(filtered)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> limitFlights(entry.getValue(), criteria.limitPerAirline()),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    /**
     * 원본 항공편 목록에 criteria 기반 필터를 적용합니다.
     *
     * @param flights 원본 항공편 목록
     * @param criteria 정규화와 검증이 완료된 검색 조건
     * @return 필터링이 끝난 항공편 목록
     */
    private List<FlightInfoResponse> applyFilters(
            List<FlightInfoResponse> flights,
            FlightSearchCriteria criteria
    ) {
        List<FlightInfoResponse> filtered = flights == null ? List.of() : flights;

        if (criteria.afterTime() != null) {
            filtered = timeFilterAgent.filterAfterTime(filtered, criteria.afterTime());
        }

        if (criteria.beforeTime() != null) {
            filtered = timeFilterAgent.filterBeforeTime(filtered, criteria.beforeTime());
        }

        if (criteria.minPrice() != null || criteria.maxPrice() != null) {
            filtered = priceFilterAgent.filterByPriceRange(
                    filtered,
                    criteria.minPrice(),
                    criteria.maxPrice()
            );
        }

        return filtered;
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
}
