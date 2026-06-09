package com.nhnacademy.flyschedule.dto.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlightInfoResponse (
        @JsonProperty("vihicleId")
        String flightId,            // 항공편명 (예: OZ8141)

        @JsonProperty("airlineNm")
        String airlineName,         // 항공사명 (예: 아시아나항공)

        @JsonProperty("depPlandTime")
        String departureTime,       // 출발시간(YYYYMMDD)

        @JsonProperty("arrPlandTime")
        String arrivalTime,         // 도착시간(YYYYMMDD)

        @JsonProperty("economyCharge")
        Integer economyCharge,       // 일반석운임(단위 : 원)

        @JsonProperty("prestigeCharge")
        Integer prestigeCharge,      // 비즈니스석운임(단위 :원)

        @JsonProperty("depAirportNm")
        String departureAirport,    // 출발 공항명

        @JsonProperty("arrAirportNm")
        String arrivalAirport       // 도착 공항명
) {}
