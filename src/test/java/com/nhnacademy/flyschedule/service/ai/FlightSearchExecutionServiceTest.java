package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightSearchAgent;
import com.nhnacademy.flyschedule.service.agent.FlightSearchConditionAgent;
import com.nhnacademy.flyschedule.service.agent.FlightSearchResultFilterAgent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSearchExecutionServiceTest {

    @Mock
    private FlightSearchConditionAgent flightSearchConditionAgent;

    @Mock
    private FlightSearchAgent flightSearchAgent;

    @Mock
    private FlightSearchResultFilterAgent flightSearchResultFilterAgent;

    @InjectMocks
    private FlightSearchExecutionService flightSearchExecutionService;

    @Test
    void search_executesCommonFlightSearchPipeline() {
        FlightSearchCommand command = new FlightSearchCommand(
                "광주",
                "제주",
                "2026-06-15",
                "13:00",
                null,
                null,
                50000
        );
        FlightSearchCriteria criteria = new FlightSearchCriteria(
                "광주",
                "제주",
                LocalDate.of(2026, 6, 15),
                LocalTime.of(13, 0),
                null,
                null,
                50000,
                3

        );
        List<FlightInfoResponse> rawFlights = List.of(
                createFlight("OZ1001", "아시아나항공")
        );
        Map<String, List<FlightInfoResponse>> filteredFlightsByAirline = Map.of(
                "아시아나항공",
                List.of(createFlight("OZ1002", "아시아나항공"))
        );

        when(flightSearchConditionAgent.normalizeAndValidate(command))
                .thenReturn(criteria);
        when(flightSearchAgent.search("광주", "제주", "20260615"))
                .thenReturn(rawFlights);
        when(flightSearchResultFilterAgent.apply(rawFlights, criteria))
                .thenReturn(filteredFlightsByAirline);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchExecutionService.search(command);

        assertEquals(filteredFlightsByAirline, result);
        verify(flightSearchConditionAgent).normalizeAndValidate(command);
        verify(flightSearchAgent).search("광주", "제주", "20260615");
        verify(flightSearchResultFilterAgent).apply(rawFlights, criteria);
    }

    private FlightInfoResponse createFlight(
            String flightId,
            String airlineName
    ) {
        return new FlightInfoResponse(
                flightId,
                airlineName,
                "202606151300",
                "202606151400",
                50000,
                0,
                "광주",
                "제주"
        );
    }
}
