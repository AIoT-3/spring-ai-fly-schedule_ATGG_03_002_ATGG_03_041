package com.nhnacademy.flyschedule.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AirPortCodeAgentTest {

    @Autowired
    private AirPortCodeAgent airPortCodeAgent;

    @Test
    void getAirportCodeTest() {
        String airportCode = airPortCodeAgent.getAirportCode("광주");
        assertNotNull(airportCode);
        assertEquals("NAARKJJ",airportCode);
    }
}