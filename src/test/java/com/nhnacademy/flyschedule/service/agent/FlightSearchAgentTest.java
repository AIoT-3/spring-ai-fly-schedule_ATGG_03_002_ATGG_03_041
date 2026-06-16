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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    FlightSearchAgent flightSearchAgent;

    private List<FlightInfoResponse> apiFlights;

    @BeforeEach
    void setUp() {
        List<FlightInfoResponse> asianaFlights = List.of(
                createFlight("OZ1001", "아시아나항공", "202606101000", 50000),
                createFlight("OZ1002", "아시아나항공", "202606101300", 70000)
        );

        List<FlightInfoResponse> koreanFlights = List.of(
                createFlight("KAL1601", "대한항공", "202606101040", 50000),
                createFlight("KAL1602", "대한항공", "202606101310", 70000)
        );

        apiFlights = List.of(
                asianaFlights.get(0),
                asianaFlights.get(1),
                koreanFlights.get(0),
                koreanFlights.get(1)
        );

        when(dateParserAgent.parseDate("내일")).thenReturn("20260610");
        when(airportCodeAgent.getAirportCode("광주")).thenReturn("NAARKJJ");
        when(airportCodeAgent.getAirportCode("제주")).thenReturn("NAARKPC");
        when(tagoApiService.getFlightInfoList(any(FlightInfoRequest.class))).thenReturn(apiFlights);
    }

    @Test
    void testSearch() {
        List<FlightInfoResponse> result =
                flightSearchAgent.search("광주", "제주", "내일");

        assertEquals(apiFlights, result);

        verify(dateParserAgent).parseDate("내일");
        verify(airportCodeAgent).getAirportCode("광주");
        verify(airportCodeAgent).getAirportCode("제주");
        verify(tagoApiService).getFlightInfoList(any(FlightInfoRequest.class));
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