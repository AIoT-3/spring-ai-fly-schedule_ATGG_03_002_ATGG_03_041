package com.nhnacademy.flyschedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TagoApiResponseWrapper<T> (
        @JsonProperty("response")
        TagoResponse<T> response
) {
        public record TagoResponse<T>(
                @JsonProperty("header")
                Header header,
                @JsonProperty("body")
                Body<T> body
        ) {}

        public record Header (
                @JsonProperty("resultCode")
                String resultCode,
                @JsonProperty("resultMsg")
                String resultMessage
        ) {}

        public record Body<T>(
                @JsonProperty("items")
                Items<T> items,

                @JsonProperty("numOfRows")
                Integer numOfRows,

                @JsonProperty("pageNo")
                Integer pageNo,

                @JsonProperty("totalCount")
                Integer totalCount
        ) {}

        public record Items<T>(
                @JsonProperty("item")
                List<T> item
        ) {}
}
