package com.nhnacademy.flyschedule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TagoApiServiceTest {

    @Autowired
    TagoApiService tagoApiService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getFlightInfoList() throws JsonProcessingException {
        FlightInfoRequest flightInfoRequest = new FlightInfoRequest(
                "NAARKJJ",
                "NAARKPC",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );

        List<FlightInfoResponse> flightInfoList = tagoApiService.getFlightInfoList(flightInfoRequest);

        String json = objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(flightInfoList);

        log.info("flightInfoList: {}", json);
    }

    @Test
    void getAirportInfoList() throws JsonProcessingException {
        List<AirportInfoResponse> airportInfoList = tagoApiService.getAirportInfoList();

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(airportInfoList);

        log.info("AirportInfoList: {}", json);
    }

    @Test
    void getAirlineInfoList() throws JsonProcessingException {
        List<AirlineInfoResponse> airlineInfoList = tagoApiService.getAirlineInfoList();

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(airlineInfoList);

        log.info("AirlineInfoList: {}", json);
    }
}