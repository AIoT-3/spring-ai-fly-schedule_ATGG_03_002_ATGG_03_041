package com.nhnacademy.flyschedule.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class DataParserAgentTest {
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    private DataParserAgent dataParserAgent;

    @Test
    void parse() {
        String date = dataParserAgent.parseDate("내일");
        assertNotNull(date);
        log.info("date: {}", date);
        assertEquals(LocalDate.now().plusDays(1).format(API_DATE_FORMAT),date);

    }

    @Test
    void parseInput() {
        String date = dataParserAgent.parseDate("2026-10-20");
        assertNotNull(date);
        log.info("date: {}", date);
        assertEquals("20261020",date);
    }
}