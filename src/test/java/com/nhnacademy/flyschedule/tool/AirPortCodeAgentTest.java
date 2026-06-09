package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirPortCodeAgentTest {

    @Mock
    private TagoApiService tagoApiService;

    private AirPortCodeAgent airPortCodeAgent;

    @BeforeEach
    void setUp() {

        when(tagoApiService.getAirportInfoList())
                .thenReturn(List.of(
                        new AirportInfoResponse("광주", "NAARKJJ"),
                        new AirportInfoResponse("인천국제공항", "RKSI"),
                        new AirportInfoResponse("김포국제공항", "RKSS")
                ));

        airPortCodeAgent = new AirPortCodeAgent(tagoApiService);

        // @PostConstruct 수동 호출
        airPortCodeAgent.init();
    }

    @Test
    void getAirportCodeTest() {
        String airportCode = airPortCodeAgent.getAirportCode("광주 공항");

        assertNotNull(airportCode);
        assertEquals("NAARKJJ", airportCode);
    }

    @Test
    void getAirportCodeGivenCode() {
        String airportCode = airPortCodeAgent.getAirportCode("NAARKJJ");

        assertNotNull(airportCode);
        assertEquals("NAARKJJ", airportCode);
    }

    @Test
    void invalidAirportCodeTest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> airPortCodeAgent.getAirportCode("")
        );
    }

    @Test
    void getAirportNameTest2() {
        String airportCode = airPortCodeAgent.getAirportCode("광주");

        assertNotNull(airportCode);
        assertEquals("NAARKJJ", airportCode);
    }
}