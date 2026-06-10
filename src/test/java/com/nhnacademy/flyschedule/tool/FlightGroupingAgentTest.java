package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightGroupingAgent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlightGroupingAgentTest {
    private final FlightGroupingAgent flightGroupingAgent = new FlightGroupingAgent();

    @Test
    void testGroupByAirline() {
        FlightInfoResponse jinAir1 = flight("LJ1001", "진에어");
        FlightInfoResponse asiana1 = flight("OZ2001", "아시아나항공");
        FlightInfoResponse jinAir2 = flight("LJ1002", "진에어");

        Map<String, List<FlightInfoResponse>> result = flightGroupingAgent.groupByAirline(
                List.of(jinAir1, asiana1, jinAir2)
        );

        assertEquals(2, result.size());
        assertIterableEquals(List.of(jinAir1, jinAir2), result.get("진에어"));
        assertIterableEquals(List.of(asiana1), result.get("아시아나항공"));
    }

    @Test
    void testGroupByAirline_EmptyList() {
        Map<String, List<FlightInfoResponse>> result = flightGroupingAgent.groupByAirline(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void testGroupByAirline_Null() {
        assertThrows(NullPointerException.class,
                () -> flightGroupingAgent.groupByAirline(null));
    }

    private FlightInfoResponse flight(String flightId, String airlineName) {
        return new FlightInfoResponse(
                flightId,
                airlineName,
                "202601010900",
                "202601011000",
                100000,
                200000,
                "김포",
                "제주"
        );
    }
}
