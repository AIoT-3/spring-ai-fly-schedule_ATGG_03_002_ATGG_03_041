package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
@SpringBootTest
class AgentIntegrationTest {
    @Autowired
    private TagoApiService service;

    @Autowired
    private DataParserAgent dataParserAgent;

    @Autowired
    private AirPortCodeAgent airPortCodeAgent;

    @Autowired
    private TimeFilterAgent timeFilterAgent;

    @Autowired
    private FlightGroupingAgent flightGroupingAgent;

    @Test
    void agentIntegrationTest() {
        String parseDate = dataParserAgent.parseDate("내일");
        String depAirportId = airPortCodeAgent.getAirportCode("광주공항");
        String arrAirportId = airPortCodeAgent.getAirportCode("제주국제공항");

        FlightInfoRequest request = new FlightInfoRequest(depAirportId, arrAirportId, parseDate);

        List<FlightInfoResponse> flightInfoList = service.getFlightInfoList(request);

        log.info("=== 내일 광주에서 제주가는 비행기 조회 ===");
        for (FlightInfoResponse flightInfoResponse : flightInfoList) {
            log.info(flightInfoResponse.toString());
        }

        log.info("=== 항공사별로 조회 ===");
        Map<String, List<FlightInfoResponse>> stringListMap = flightGroupingAgent.groupByAirline(flightInfoList);
        for (Entry<String, List<FlightInfoResponse>> entry : stringListMap.entrySet()) {
            log.info(entry.getKey());
            for (FlightInfoResponse flightInfoResponse : entry.getValue()) {
                log.info(flightInfoResponse.toString());
            }
        }

        log.info("=== 1시 이전 조회 ===");
        List<FlightInfoResponse> beforeInfo = timeFilterAgent.filterBeforeTime(flightInfoList, LocalTime.of(13, 0));
        for (FlightInfoResponse flightInfoResponse : beforeInfo) {
            log.info(flightInfoResponse.toString());
        }

        log.info("=== 1시 이후 조회 ===");
        List<FlightInfoResponse> afterInfo = timeFilterAgent.filterAfterTime(flightInfoList, LocalTime.of(13, 0));
        for (FlightInfoResponse flightInfoResponse : afterInfo) {
            log.info(flightInfoResponse.toString());
        }


    }

}
