package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchResultFilterAgentTest {

    private final FlightSearchResultFilterAgent flightSearchResultFilterAgent =
            new FlightSearchResultFilterAgent(
                    new TimeFilterAgent(),
                    new PriceFilterAgent(),
                    new FlightGroupingAgent()
            );

    @Test
    void apply_filtersByTimePriceAndLimitPerAirline() {
        List<FlightInfoResponse> flights = List.of(
                createFlight("OZ1001", "아시아나항공", "202606101000", 40000),
                createFlight("OZ1002", "아시아나항공", "202606101300", 60000),
                createFlight("OZ1003", "아시아나항공", "202606101430", 45000),
                createFlight("OZ1004", "아시아나항공", "202606101805", 45000),
                createFlight("KE1001", "대한항공", "202606101200", 45000),
                createFlight("KE1002", "대한항공", "202606101500", 49000),
                createFlight("KE1003", "대한항공", "202606101700", 50000)
        );

        FlightSearchCriteria criteria = new FlightSearchCriteria(
                "광주",
                "제주",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(13, 0),
                LocalTime.of(18, 0),
                null,
                50000,
                1
        );

        Map<String, List<FlightInfoResponse>> result =
                flightSearchResultFilterAgent.apply(flights, criteria);

        assertEquals(List.of("아시아나항공", "대한항공"), result.keySet().stream().toList());
        assertEquals(1, result.get("아시아나항공").size());
        assertEquals("OZ1003", result.get("아시아나항공").getFirst().flightId());
        assertEquals(1, result.get("대한항공").size());
        assertEquals("KE1002", result.get("대한항공").getFirst().flightId());
    }

    @Test
    void apply_returnsEmptyMapWhenFlightsAreEmpty() {
        FlightSearchCriteria criteria = new FlightSearchCriteria(
                "광주",
                "제주",
                LocalDate.of(2026, 6, 10),
                null,
                null,
                null,
                null,
                3
        );

        assertTrue(flightSearchResultFilterAgent.apply(List.of(), criteria).isEmpty());
    }

    @Test
    void apply_rejectsNullCriteria() {
        assertThrows(
                IllegalArgumentException.class,
                () -> flightSearchResultFilterAgent.apply(List.of(), null)
        );
    }

    private FlightInfoResponse createFlight(
            String flightId,
            String airlineName,
            String departureTime,
            Integer economyCharge
    ) {
        return new FlightInfoResponse(
                flightId,
                airlineName,
                departureTime,
                null,
                economyCharge,
                0,
                "광주",
                "제주"
        );
    }
}
