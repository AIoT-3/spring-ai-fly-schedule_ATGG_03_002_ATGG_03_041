package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSearchOrchestratorTest {

    @Mock
    private FlightSearchExecutionService flightSearchExecutionService;

    @InjectMocks
    private FlightSearchOrchestrator flightSearchOrchestrator;

    @Test
    void searchFlightsWithFilters_delegatesToCommonExecutionService() {
        FlightSearchCommand command = new FlightSearchCommand(
                "광주",
                "제주",
                "내일",
                "13:00",
                "18:00",
                null,
                50000
        );
        Map<String, List<FlightInfoResponse>> flightsByAirline = Map.of(
                "아시아나항공",
                List.of(createFlight("OZ1003", "아시아나항공", "202606101430", 45000))
        );

        when(flightSearchExecutionService.search(command))
                .thenReturn(flightsByAirline);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchOrchestrator.searchFlightsWithFilters(
                        "광주",
                        "제주",
                        "내일",
                        "13:00",
                        "18:00",
                        null,
                        50000
                );

        assertEquals(flightsByAirline, result);
        verify(flightSearchExecutionService).search(command);
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
