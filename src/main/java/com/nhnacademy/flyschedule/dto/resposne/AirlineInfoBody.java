package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirlineInfoBody(
        @JsonProperty("items")
        TagoApiResponse.Items<Item> items
) {
    public record Item(
            @JsonProperty("airlineNm")
            String airlineName,

            @JsonProperty("airlineId")
            String airlineId
    ) {}
}

