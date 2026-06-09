package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirportInfoResponse (
        @JsonProperty("airportNm")
        String airportName,

        @JsonProperty("airportId")
        String airportId
) {}
