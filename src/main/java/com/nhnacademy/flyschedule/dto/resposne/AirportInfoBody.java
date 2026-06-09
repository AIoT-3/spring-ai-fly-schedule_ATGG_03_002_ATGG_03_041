package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirportInfoBody(
        @JsonProperty("items")
        TagoApiResponse.Items<Item> items
) {
    public record Item(
            @JsonProperty("airportNm")
            String airportName,

            @JsonProperty("airportId")
            String airportId
    ) {}
}

