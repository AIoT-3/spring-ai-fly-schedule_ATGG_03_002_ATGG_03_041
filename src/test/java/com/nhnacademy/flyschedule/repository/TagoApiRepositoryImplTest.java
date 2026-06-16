package com.nhnacademy.flyschedule.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.response.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag("integration")
@Slf4j
@SpringBootTest
class TagoApiRepositoryImplTest {

    @Autowired
    TagoApiRepository tagoApiRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetFlightInfoList() throws JsonProcessingException {
        FlightInfoRequest flightInfoRequest = new FlightInfoRequest(
                "NAARKJJ",
                "NAARKPC",
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        );

        List<FlightInfoResponse> flightInfoList =
                tagoApiRepository.getFlightInfoList(flightInfoRequest);

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(flightInfoList);

        log.info("flightInfoList: {}", json);
    }

    @Test
    void testGetAirportInfoList() throws JsonProcessingException {
        List<AirportInfoResponse> airportInfoList =
                tagoApiRepository.getAirportInfoList();

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(airportInfoList);

        log.info("AirportInfoList: {}", json);
    }

    @Test
    void testGetAirlineInfoList() throws JsonProcessingException {
        List<AirlineInfoResponse> airlineInfoList =
                tagoApiRepository.getAirlineInfoList();

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(airlineInfoList);

        log.info("AirlineInfoList: {}", json);
    }
}
