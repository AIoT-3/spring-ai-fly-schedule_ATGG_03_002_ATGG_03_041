package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightSearchConditionAgentTest {

    private final FlightSearchConditionAgent flightSearchConditionAgent =
            new FlightSearchConditionAgent(new DateParserAgent());

    @Test
    void normalizeAndValidate_trimsConvertsAndSwapsPriceRange() {
        FlightSearchCommand command = new FlightSearchCommand(
                " 광주 ",
                " 제주 ",
                "내일",
                "13:00",
                "18:00",
                60000,
                40000
        );

        FlightSearchCriteria criteria =
                flightSearchConditionAgent.normalizeAndValidate(command);

        assertEquals("광주", criteria.departure());
        assertEquals("제주", criteria.arrival());
        assertEquals(LocalDate.now().plusDays(1), criteria.date());
        assertEquals(LocalTime.of(13, 0), criteria.afterTime());
        assertEquals(LocalTime.of(18, 0), criteria.beforeTime());
        assertEquals(40000, criteria.minPrice());
        assertEquals(60000, criteria.maxPrice());
        assertEquals(3, criteria.limitPerAirline());
    }

    @Test
    void normalizeAndValidate_removesNoEffectTimeAndInvalidPriceConditions() {
        FlightSearchCommand command = new FlightSearchCommand(
                "광주",
                "제주",
                "2026-06-15",
                "00:00",
                "23:59",
                0,
                -1
        );

        FlightSearchCriteria criteria =
                flightSearchConditionAgent.normalizeAndValidate(command);

        assertEquals(LocalDate.of(2026, 6, 15), criteria.date());
        assertNull(criteria.afterTime());
        assertNull(criteria.beforeTime());
        assertNull(criteria.minPrice());
        assertNull(criteria.maxPrice());
        assertEquals(3, criteria.limitPerAirline());
    }

    @Test
    void normalizeAndValidate_rejectsBlankRequiredFields() {
        FlightSearchCommand command = new FlightSearchCommand(
                "null",
                "제주",
                "내일",
                null,
                null,
                null,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> flightSearchConditionAgent.normalizeAndValidate(command)
        );
    }

    @Test
    void normalizeAndValidate_rejectsInvalidDate() {
        FlightSearchCommand command = new FlightSearchCommand(
                "광주",
                "제주",
                "2026년 6월 15일",
                null,
                null,
                null,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> flightSearchConditionAgent.normalizeAndValidate(command)
        );
    }

    @Test
    void normalizeAndValidate_rejectsInvalidTime() {
        FlightSearchCommand command = new FlightSearchCommand(
                "광주",
                "제주",
                "내일",
                "25:00",
                null,
                null,
                null
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> flightSearchConditionAgent.normalizeAndValidate(command)
        );
    }
}
