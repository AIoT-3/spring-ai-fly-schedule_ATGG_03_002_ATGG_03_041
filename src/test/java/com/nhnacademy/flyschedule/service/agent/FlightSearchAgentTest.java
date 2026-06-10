package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSearchAgentTest {

    @Mock
    TagoApiService tagoApiService;

    @Mock
    DateParserAgent dateParserAgent;

    @Mock
    AirportCodeAgent airportCodeAgent;

    @Mock
    FlightGroupingAgent flightGroupingAgent;

    @Mock
    TimeFilterAgent timeFilterAgent;

    @Mock
    PriceFilterAgent priceFilterAgent;

    @InjectMocks
    FlightSearchAgent flightSearchAgent;

    private List<FlightInfoResponse> asianaFlights;
    private List<FlightInfoResponse> koreanFlights;
    private Map<String, List<FlightInfoResponse>> flightInfoResponseMap;

    @BeforeEach
    void setUp() {
        asianaFlights = List.of(
                createFlight("OZ1001", "아시아나항공", "202606101000", 50000),
                createFlight("OZ1002", "아시아나항공", "202606101300", 70000)
        );

        koreanFlights = List.of(
                createFlight("KAL1601", "대한항공", "202606101040", 50000),
                createFlight("KAL1602", "대한항공", "202606101310", 70000)
        );

        flightInfoResponseMap = Map.of(
                "아시아나항공", asianaFlights,
                "대한항공", koreanFlights
        );

        List<FlightInfoResponse> apiFlights = List.of(
                asianaFlights.get(0),
                asianaFlights.get(1),
                koreanFlights.get(0),
                koreanFlights.get(1)
        );

        when(dateParserAgent.parseDate("내일")).thenReturn("20260610");
        when(airportCodeAgent.getAirportCode("광주")).thenReturn("NAARKJJ");
        when(airportCodeAgent.getAirportCode("제주")).thenReturn("NAARKPC");
        when(tagoApiService.getFlightInfoList(any(FlightInfoRequest.class))).thenReturn(apiFlights);
        when(flightGroupingAgent.groupByAirline(apiFlights)).thenReturn(flightInfoResponseMap);
    }

    @Test
    void testSearch() {
        Map<String, List<FlightInfoResponse>> result =
                flightSearchAgent.search("광주", "제주", "내일");

        assertEquals(flightInfoResponseMap, result);

        verify(dateParserAgent).parseDate("내일");
        verify(airportCodeAgent).getAirportCode("광주");
        verify(airportCodeAgent).getAirportCode("제주");
        verify(tagoApiService).getFlightInfoList(any(FlightInfoRequest.class));
        verify(flightGroupingAgent).groupByAirline(any());
    }

    @Test
    void testSearchWithTimeFilter() {
        List<FlightInfoResponse> filteredAsiana = List.of(asianaFlights.get(1));
        List<FlightInfoResponse> filteredKorean = List.of(koreanFlights.get(1));

        when(timeFilterAgent.filterAfterTime(asianaFlights, LocalTime.of(13, 0)))
                .thenReturn(filteredAsiana);
        when(timeFilterAgent.filterAfterTime(koreanFlights, LocalTime.of(13, 0)))
                .thenReturn(filteredKorean);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchAgent.searchWithTimeFilter("광주", "제주", "내일", "13:00");

        assertEquals(filteredAsiana, result.get("아시아나항공"));
        assertEquals(filteredKorean, result.get("대한항공"));

        verify(timeFilterAgent).filterAfterTime(asianaFlights, LocalTime.of(13, 0));
        verify(timeFilterAgent).filterAfterTime(koreanFlights, LocalTime.of(13, 0));
    }

    @Test
    void testSearchWithPriceFilter() {
        List<FlightInfoResponse> filteredAsiana = List.of(asianaFlights.get(0));
        List<FlightInfoResponse> filteredKorean = List.of(koreanFlights.get(0));

        when(priceFilterAgent.filterByPriceRange(asianaFlights, 40000, 60000))
                .thenReturn(filteredAsiana);
        when(priceFilterAgent.filterByPriceRange(koreanFlights, 40000, 60000))
                .thenReturn(filteredKorean);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchAgent.searchWithPriceFilter("광주", "제주", "내일", "40000", "60000");

        assertEquals(filteredAsiana, result.get("아시아나항공"));
        assertEquals(filteredKorean, result.get("대한항공"));

        verify(priceFilterAgent).filterByPriceRange(asianaFlights, 40000, 60000);
        verify(priceFilterAgent).filterByPriceRange(koreanFlights, 40000, 60000);
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