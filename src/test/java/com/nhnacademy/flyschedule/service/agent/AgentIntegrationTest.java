package com.nhnacademy.flyschedule.service.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class AgentIntegrationTest {

    @Autowired
    private TagoApiService service;

    @Autowired
    private DateParserAgent dateParserAgent;

    @Autowired
    private AirportCodeAgent airPortCodeAgent;

    @Autowired
    private TimeFilterAgent timeFilterAgent;

    @Autowired
    private FlightGroupingAgent flightGroupingAgent;

    @Autowired
    private PriceFilterAgent priceFilterAgent;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void agentIntegrationTest() throws Exception {

        String parseDate = dateParserAgent.parseDate("내일");
        String depAirportId = airPortCodeAgent.getAirportCode("광주공항");
        String arrAirportId = airPortCodeAgent.getAirportCode("제주국제공항");

        FlightInfoRequest request =
                new FlightInfoRequest(depAirportId, arrAirportId, parseDate);

        List<FlightInfoResponse> flightInfoList =
                service.getFlightInfoList(request);

        log.info("=== 내일 광주에서 제주가는 비행기 조회 ===");
        log.info(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(flightInfoList));

        log.info("=== 항공사별로 조회 ===");
        Map<String, List<FlightInfoResponse>> grouped =
                flightGroupingAgent.groupByAirline(flightInfoList);

        log.info(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(grouped));

        log.info("=== 1시 이전 조회 ===");
        List<FlightInfoResponse> beforeInfo =
                timeFilterAgent.filterBeforeTime(flightInfoList, LocalTime.of(13, 0));

        log.info(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(beforeInfo));

        log.info("=== 1시 이후 조회 ===");
        List<FlightInfoResponse> afterInfo =
                timeFilterAgent.filterAfterTime(flightInfoList, LocalTime.of(13, 0));

        log.info(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(afterInfo));

        log.info("=== 4만원 이상 조회 6만원 미만 조회 ===");
        List<FlightInfoResponse> priceResponse = priceFilterAgent.filterByPriceRange(flightInfoList, 40000, 60000);
        log.info(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(priceResponse));


        log.info("=== 가장 싼 항공편 조회 ===");
        FlightInfoResponse cheapestPriceFlight = priceFilterAgent.findCheapestPrice(flightInfoList);
        log.info("most cheapestPriceFlight: {}",
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(cheapestPriceFlight));

        log.info("=== 가장 비싼 항공편 조회 ===");
        FlightInfoResponse mostExpensivePriceFlight = priceFilterAgent.findMostExpensivePrice(flightInfoList);
        log.info("most cheapestPriceFlight: {}",
                objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mostExpensivePriceFlight));

        log.info("=== 평균 항공편 가격 ===");
        double price = priceFilterAgent.calculateAveragePrice(flightInfoList);
        log.info("averagePrice: {}", price);


    }
}