package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TagoApiResponse<T> (
        @JsonProperty("response")
        TagoApiResponse<T> response
) {
        public record TagoResponse<T>(
                @JsonProperty("header")
                Header header,
                @JsonProperty("body")
                T body
        ) {}

        public record Header (
                @JsonProperty("resultCode")
                String resultCode,
                @JsonProperty("resultMsg")
                String resultMessage
        ) {}

        public record Items<T>(
                @JsonProperty("item")
                List<T> item
        ) {}
}
