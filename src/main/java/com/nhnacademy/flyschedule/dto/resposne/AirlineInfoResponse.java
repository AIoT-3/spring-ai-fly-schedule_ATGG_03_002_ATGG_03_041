package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirlineInfoResponse (
        @JsonProperty("airlineNm")
        String airlineName,

        @JsonProperty("airlineId")
        String airlineId
) {}