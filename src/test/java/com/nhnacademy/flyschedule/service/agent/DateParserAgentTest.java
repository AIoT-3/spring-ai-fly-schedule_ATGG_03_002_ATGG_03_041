package com.nhnacademy.flyschedule.service.agent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DateParserAgentTest {
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DateParserAgent dateParserAgent = new DateParserAgent();

    @Test
    void parse() {
        String date = dateParserAgent.parseDate("내일");
        assertNotNull(date);
        assertEquals(LocalDate.now().plusDays(1).format(API_DATE_FORMAT), date);

    }

    @Test
    void parseInput() {
        String date = dateParserAgent.parseDate("2026-10-20");
        assertNotNull(date);
        assertEquals("20261020", date);
    }

    @Test
    void parseNull() {
        String date = dateParserAgent.parseDate(null);
        assertNotNull(date);
        assertEquals(LocalDate.now().format(API_DATE_FORMAT), date);

    }

    @Test
    void InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            dateParserAgent.parseDate("2026년 11월 20일");
        });
    }
}