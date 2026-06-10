package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
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
class AirlineCodeAgentTest {

    @Mock
    private TagoApiService tagoApiService;

    private AirlineCodeAgent airlineCodeAgent;

    @BeforeEach
    void setUp() {
        when(tagoApiService.getAirlineInfoList())
                .thenReturn(List.of(
                        new AirlineInfoResponse("대한항공", "KAL"),
                        new AirlineInfoResponse("아시아나항공", "AAR"),
                        new AirlineInfoResponse("제주항공", "JJA")
                ));

        airlineCodeAgent = new AirlineCodeAgent(tagoApiService);
        airlineCodeAgent.init();
    }

    @ParameterizedTest
    @CsvSource({
            "대한항공, KAL",
            "아시아나항공, AAR",
            "제주항공, JJA"
    })
    void testGetAirlineCode(String input, String expected) {
        assertEquals(expected, airlineCodeAgent.getAirlineCode(input));
    }

    @Test
    void testGetAirlineCode_InvalidInput() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> airlineCodeAgent.getAirlineCode("")
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> airlineCodeAgent.getAirlineCode(null)
                )
        );
    }
}
