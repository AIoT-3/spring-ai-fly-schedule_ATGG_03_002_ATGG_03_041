package com.nhnacademy.flyschedule.service.agent;


import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchAgent {
    private final TagoApiService tagoApiService;
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final FlightGroupingAgent flightGroupingAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;


    public Map<String, List<FlightInfoResponse>> search(
            String departureAirport,
            String arrivalAirport,
            String date
    ) {
        // 날짜 파싱
        String parsedDate = dateParserAgent.parseDate(date);
        // 공항 코드 변환
        String depAirportId = airportCodeAgent.getAirportCode(departureAirport);
        String arrAirportId = airportCodeAgent.getAirportCode(arrivalAirport);
        // request dto 생성
        FlightInfoRequest request = new FlightInfoRequest(
                depAirportId,
                arrAirportId,
                parsedDate
        );
        // api 호출
        List<FlightInfoResponse> flights = tagoApiService.getFlightInfoList(request);
        // 항공사별 그룹핑
        return flightGroupingAgent.groupByAirline(flights);
    }

    public Map<String, List<FlightInfoResponse>> searchWithTimeFilter(
            String departureAirport,
            String arrivalAirport,
            String date,
            String afterTime
    ) {
        Map<String, List<FlightInfoResponse>> result = search(departureAirport, arrivalAirport, date);

        LocalTime parsedAfterTime = LocalTime.parse(afterTime);

        return filterSearchResult(
                result,
                flights -> timeFilterAgent.filterAfterTime(flights, parsedAfterTime)
        );
    }

    public Map<String, List<FlightInfoResponse>> searchWithPriceFilter(
            String departureAirport,
            String arrivalAirport,
            String date,
            String minPrice,
            String maxPrice
    ) {
        Map<String, List<FlightInfoResponse>> result = search(departureAirport, arrivalAirport, date);

        Integer parsedMinPrice = parseNullableInteger(minPrice);
        Integer parsedMaxPrice = parseNullableInteger(maxPrice);

        return filterSearchResult(
                result,
                flights -> priceFilterAgent.filterByPriceRange(flights, parsedMinPrice, parsedMaxPrice)
        );
    }

    private Map<String, List<FlightInfoResponse>> filterSearchResult(
            Map<String, List<FlightInfoResponse>> searchResult,
            UnaryOperator<List<FlightInfoResponse>> filter // Function<List<FlightInfoResponse>, List<FlightInfoResponse>> filter
    ) {
        return searchResult.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> filter.apply(entry.getValue())
                ));
    }

    private Integer parseNullableInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.parseInt(value);
    }
}
