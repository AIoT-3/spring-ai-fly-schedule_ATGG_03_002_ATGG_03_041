package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.PriceFilterAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceFilterAgentTest {
    private PriceFilterAgent priceFilterAgent;
    private List<FlightInfoResponse> flights;

    @BeforeEach
    void setUp() {
        priceFilterAgent = new PriceFilterAgent();
        flights = List.of(
                flight("OZ2001", 30000),
                flight("OZ2002", 10000),
                flight("OZ2003", 40000),
                flight("OZ2004", 20000),
                flight("OZ2005", 50000),
                flight("OZ2006", null),
                flight("OZ2007", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("priceRangeCases")
    void filterByPriceRange_shouldFilterFlightsByMinAndMaxPrice(
            Integer minPrice,
            Integer maxPrice,
            List<String> expectedFlightIds
    ) {
        List<FlightInfoResponse> filteredFlights = priceFilterAgent.filterByPriceRange(
                flights,
                minPrice,
                maxPrice
        );

        assertEquals(
                expectedFlightIds,
                filteredFlights.stream()
                        .map(FlightInfoResponse::flightId)
                        .toList()
        );
    }

    @Test
    void filterByPriceRange_shouldReturnEmptyList_whenFlightsAreNullOrEmpty() {
        assertTrue(priceFilterAgent.filterByPriceRange(null, 10000, 50000).isEmpty());
        assertTrue(priceFilterAgent.filterByPriceRange(List.of(), 10000, 50000).isEmpty());
    }

    private static Stream<Arguments> priceRangeCases() {
        return Stream.of(
                Arguments.of(20000, 40000, List.of("OZ2001", "OZ2003", "OZ2004")),
                Arguments.of(null, 30000, List.of("OZ2001", "OZ2002", "OZ2004")),
                Arguments.of(30000, null, List.of("OZ2001", "OZ2003", "OZ2005")),
                Arguments.of(null, null, List.of("OZ2001", "OZ2002", "OZ2003", "OZ2004", "OZ2005")),
                Arguments.of(30000, 30000, List.of("OZ2001")),
                Arguments.of(60000, 70000, List.of())
        );
    }

    private FlightInfoResponse flight(String flightId, Integer economyCharge) {
        return new FlightInfoResponse(
                flightId,
                null,
                null,
                null,
                economyCharge,
                null,
                null,
                null
        );
    }
}
