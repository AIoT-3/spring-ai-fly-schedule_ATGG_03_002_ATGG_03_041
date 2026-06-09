package com.nhnacademy.flyschedule.dto.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotNull;

/**
 * 항공편 조회 요청 dto
 *
 * @param depAirportId 출발 공항 ID (예: NAARKJJ)
 * @param arrAirportId 도착 공항 ID (예: NAARKPC)
 * @param departmentDate 조회 일자 (YYYYMMDD)
 */
public record FlightInfoRequest (
        @NotNull
        String depAirportId,
        @NotNull
        String arrAirportId,
        @NotNull
        String departmentDate
) {
    public MultiValueMap<String, String> toQueryParams(String serviceKey) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        queryParams.add("serviceKey", serviceKey);
        queryParams.add("_type", "json");

        queryParams.add("depAirportId", depAirportId);
        queryParams.add("arrAirportId", arrAirportId);
        queryParams.add("depPlandTime", departmentDate);

        return queryParams;
    }
}