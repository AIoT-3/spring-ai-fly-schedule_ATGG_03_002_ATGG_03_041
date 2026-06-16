package com.nhnacademy.flyschedule.service.agent;


import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightSearchAgent {
    private final TagoApiService tagoApiService;
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;

    public List<FlightInfoResponse> search(
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
        return tagoApiService.getFlightInfoList(request);
    }
}
