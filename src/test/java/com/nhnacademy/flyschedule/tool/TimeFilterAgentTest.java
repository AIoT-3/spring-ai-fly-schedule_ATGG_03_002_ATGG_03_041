package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.TimeFilterAgent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeFilterAgentTest {

    private final TimeFilterAgent timeFilterAgent = new TimeFilterAgent();

    private final List<FlightInfoResponse> flights = List.of(
            new FlightInfoResponse("OZ8141","아시아나항공","202606091000","202606091055",49800,0,"광주","제주"),
            new FlightInfoResponse("OZ8143","아시아나항공","202606091300","202606091355",49800,0,"광주","제주"),
            new FlightInfoResponse("OZ8145","아시아나항공","202606091440","202606091535",49800,0,"광주","제주"),
            new FlightInfoResponse("OZ8147","아시아나항공","202606091805","202606091900",49800,0,"광주","제주")
    );

    @Test
    @DisplayName("1시 이후 항공편 필터링")
    void after_time_filter_test() {

        List<FlightInfoResponse> result =
                timeFilterAgent.filterAfterTime(flights, LocalTime.of(13, 0));

        assertFalse(result.isEmpty());

        for (FlightInfoResponse f : result) {
            assertTrue(f.departureTime().compareTo("202606091300") >= 0);
        }
    }

    @Test
    @DisplayName("1시 이전 항공편 필터링")
    void before_time_filter_test() {

        List<FlightInfoResponse> result =
                timeFilterAgent.filterBeforeTime(flights, LocalTime.of(13, 0));

        assertFalse(result.isEmpty());

        for (FlightInfoResponse f : result) {
            assertTrue(f.departureTime().compareTo("202606091300") <= 0);
        }
    }
}