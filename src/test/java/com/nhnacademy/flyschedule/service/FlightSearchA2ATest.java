package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.ai.FlightSearchA2A;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class FlightSearchA2ATest {

    @Autowired
    private FlightSearchA2A flightSearchA2A;

    @Test
    void TestFlightSearchA2A() {
        Map<String, List<FlightInfoResponse>> search =
                flightSearchA2A.search("Tool 을 활용해서 내일 10시이후 광주에서 제주도 가는 비행기표 조회해줘 ");

        search.forEach((airlineName, flights) ->
                flights.forEach(flightInfoResponse ->
                        log.info("{}: {}", airlineName, flightInfoResponse)
                )
        );
    }
}
