package com.nhnacademy.flyschedule.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchCommandTest {

    @Test
    void fromExtractResult_convertsExtractResultToCommand() {
        FlightSearchExtractResult extractResult = new FlightSearchExtractResult(
                "광주",
                "제주",
                "2026-06-16",
                "13:00",
                "18:00",
                40000,
                60000
        );

        FlightSearchCommand command = FlightSearchCommand.fromExtractResult(extractResult);

        assertEquals("광주", command.departure());
        assertEquals("제주", command.arrival());
        assertEquals("2026-06-16", command.date());
        assertEquals("13:00", command.afterTime());
        assertEquals("18:00", command.beforeTime());
        assertEquals(40000, command.minPrice());
        assertEquals(60000, command.maxPrice());
    }

    @Test
    void fromExtractResult_rejectsNullResult() {
        assertThrows(
                IllegalArgumentException.class,
                () -> FlightSearchCommand.fromExtractResult(null)
        );
    }

    @Test
    void fromToolArguments_convertsToolArgumentsToCommand() {
        FlightSearchCommand command = FlightSearchCommand.fromToolArguments(
                "김포",
                "부산",
                "내일",
                "09:00",
                null,
                null,
                70000
        );

        assertEquals("김포", command.departure());
        assertEquals("부산", command.arrival());
        assertEquals("내일", command.date());
        assertEquals("09:00", command.afterTime());
        assertNull(command.beforeTime());
        assertNull(command.minPrice());
        assertEquals(70000, command.maxPrice());
    }
}
