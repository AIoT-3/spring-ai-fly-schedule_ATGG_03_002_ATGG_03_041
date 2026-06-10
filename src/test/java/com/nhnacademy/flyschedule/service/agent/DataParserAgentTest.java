package com.nhnacademy.flyschedule.service.agent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DataParserAgentTest {
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DataParserAgent dataParserAgent = new DataParserAgent();

    @Test
    void parse() {
        String date = dataParserAgent.parseDate("내일");
        assertNotNull(date);
        assertEquals(LocalDate.now().plusDays(1).format(API_DATE_FORMAT), date);

    }

    @Test
    void parseInput() {
        String date = dataParserAgent.parseDate("2026-10-20");
        assertNotNull(date);
        assertEquals("20261020", date);
    }

    @Test
    void parseNull() {
        String date = dataParserAgent.parseDate(null);
        assertNotNull(date);
        assertEquals(LocalDate.now().format(API_DATE_FORMAT), date);

    }

    @Test
    void InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            dataParserAgent.parseDate("2026년 11월 20일");
        });
    }
}