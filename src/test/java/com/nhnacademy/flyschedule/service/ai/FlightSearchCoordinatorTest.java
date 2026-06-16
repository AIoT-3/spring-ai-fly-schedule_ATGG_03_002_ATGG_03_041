package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.dto.ModelType;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightSearchCoordinatorTest {

    @Mock
    private FlightSearchExtractService flightSearchExtractService;

    @Mock
    private FlightSearchExecutionService flightSearchExecutionService;

    @InjectMocks
    private FlightSearchCoordinator flightSearchCoordinator;

    @Test
    void search_convertsExtractResultToCommandAndDelegatesToExecutionService() {
        String message = "내일 13시 이후 광주에서 제주 가는 항공편 찾아줘";
        FlightSearchExtractResult extractResult = new FlightSearchExtractResult(
                "광주",
                "제주",
                "2026-06-16",
                "13:00",
                null,
                null,
                50000
        );
        FlightSearchCommand expectedCommand = FlightSearchCommand.fromExtractResult(extractResult);
        Map<String, List<FlightInfoResponse>> expectedResult = Map.of(
                "아시아나항공",
                List.of(createFlight("OZ1001", "아시아나항공"))
        );

        when(flightSearchExtractService.extractFlightSearch(message, ModelType.GEMINI))
                .thenReturn(extractResult);
        when(flightSearchExecutionService.search(expectedCommand))
                .thenReturn(expectedResult);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchCoordinator.search(message, ModelType.GEMINI);

        assertEquals(expectedResult, result);
        verify(flightSearchExtractService).extractFlightSearch(message, ModelType.GEMINI);
        verify(flightSearchExecutionService).search(expectedCommand);
    }

    @Test
    void search_usesDefaultModelWhenModelTypeIsNull() {
        String message = "내일 광주에서 제주 가는 항공편 찾아줘";
        FlightSearchExtractResult extractResult = new FlightSearchExtractResult(
                "광주",
                "제주",
                "2026-06-16",
                null,
                null,
                null,
                null
        );
        FlightSearchCommand expectedCommand = FlightSearchCommand.fromExtractResult(extractResult);
        Map<String, List<FlightInfoResponse>> expectedResult = Map.of();

        when(flightSearchExtractService.extractFlightSearch(message, ModelType.defaultModel))
                .thenReturn(extractResult);
        when(flightSearchExecutionService.search(expectedCommand))
                .thenReturn(expectedResult);

        Map<String, List<FlightInfoResponse>> result =
                flightSearchCoordinator.search(message, null);

        assertEquals(expectedResult, result);
        verify(flightSearchExtractService).extractFlightSearch(message, ModelType.defaultModel);
        verify(flightSearchExecutionService).search(expectedCommand);
    }

    @Test
    void search_rejectsBlankMessageBeforeExtraction() {
        assertThrows(
                IllegalArgumentException.class,
                () -> flightSearchCoordinator.search(" ", ModelType.OLLAMA)
        );

        verifyNoInteractions(flightSearchExtractService, flightSearchExecutionService);
    }

    private FlightInfoResponse createFlight(String flightId, String airlineName) {
        return new FlightInfoResponse(
                flightId,
                airlineName,
                "202606161300",
                "202606161400",
                50000,
                0,
                "광주",
                "제주"
        );
    }
}
