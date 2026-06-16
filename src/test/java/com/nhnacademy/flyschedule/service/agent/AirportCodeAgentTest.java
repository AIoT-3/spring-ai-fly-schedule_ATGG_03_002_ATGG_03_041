package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirportCodeAgentTest {

    @Mock
    private TagoApiService tagoApiService;

    private AirportCodeAgent airPortCodeAgent;

    @BeforeEach
    void setUp() {
        when(tagoApiService.getAirportInfoList())
                .thenReturn(List.of(
                        new AirportInfoResponse("광주", "NAARKJJ"),
                        new AirportInfoResponse("인천", "NAARKSI"),
                        new AirportInfoResponse("김포", "NAARKSS")
                ));

        airPortCodeAgent = new AirportCodeAgent(tagoApiService);
        airPortCodeAgent.init();
    }

    @ParameterizedTest
    @CsvSource({
            "광주 공항, NAARKJJ",
            "광주, NAARKJJ",
            "NAARKJJ, NAARKJJ"
    })
    void getAirportCodeTest(String input, String expected) {
        assertEquals(expected,
                airPortCodeAgent.getAirportCode(input));
    }

    @Test
    void invalidAirportCodeTest() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> airPortCodeAgent.getAirportCode("")
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> airPortCodeAgent.getAirportCode("*******")
                )
        );
    }
}