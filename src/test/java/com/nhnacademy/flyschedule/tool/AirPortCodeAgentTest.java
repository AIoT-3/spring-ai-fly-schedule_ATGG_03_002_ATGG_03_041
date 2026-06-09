package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
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
class AirPortCodeAgentTest {

    @Mock
    private TagoApiService tagoApiService;

    private AirPortCodeAgent airPortCodeAgent;

    @BeforeEach
    void setUp() {
        when(tagoApiService.getAirportInfoList())
                .thenReturn(List.of(
                        new AirportInfoResponse("광주", "NAARKJJ"),
                        new AirportInfoResponse("인천", "NAARKSI"),
                        new AirportInfoResponse("김포", "NAARKSS")
                ));

        airPortCodeAgent = new AirPortCodeAgent(tagoApiService);
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