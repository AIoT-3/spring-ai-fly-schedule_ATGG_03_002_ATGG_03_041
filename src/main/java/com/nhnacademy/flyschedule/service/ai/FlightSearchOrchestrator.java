package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchOrchestrator {
    private final FlightSearchExecutionService flightSearchExecutionService;

    public Map<String, List<FlightInfoResponse>> searchFlightsWithFilters(
            String departure,
            String arrival,
            String date,
            String afterTime,
            String beforeTime,
            Integer minPrice,
            Integer maxPrice
    ) {
        log.info("Orchestrator flight search started: departure={}, arrival={}, date={}, afterTime={}, beforeTime={}, minPrice={}, maxPrice={}",
                departure,
                arrival,
                date,
                afterTime,
                beforeTime,
                minPrice,
                maxPrice);

        log.info("Orchestrator converting tool arguments to command");
        FlightSearchCommand command = FlightSearchCommand.fromToolArguments(
                departure,
                arrival,
                date,
                afterTime,
                beforeTime,
                minPrice,
                maxPrice
        );
        log.info("Orchestrator command created: {}", command);

        try {
            log.info("Orchestrator executing common flight search pipeline");
            Map<String, List<FlightInfoResponse>> result = flightSearchExecutionService.search(command);
            log.info("Orchestrator flight search completed: airlineCount={}, flightCount={}",
                    result.size(),
                    countFlights(result));

            return result;
        } catch (IllegalArgumentException e) {
            log.warn("Orchestrator rejected invalid flight search command: {}", command, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Orchestrator flight search failed: {}", command, e);
            throw e;
        }
    }

    private int countFlights(Map<String, List<FlightInfoResponse>> flightsByAirline) {
        return flightsByAirline.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }
}
