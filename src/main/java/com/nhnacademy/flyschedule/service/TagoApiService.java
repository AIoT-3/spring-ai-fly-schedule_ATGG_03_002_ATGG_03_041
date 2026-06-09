package com.nhnacademy.flyschedule.service;


import com.nhnacademy.flyschedule.config.DataGoKrApiProperties;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.TagoApiResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagoApiService {
    private final DataGoKrApiProperties apiProperties;
    private final RestClient restClient = RestClient.create();

    public List<FlightInfoResponse> getFlightInfoList(FlightInfoRequest request) {
        return requestApi(
                "/GetFlightOpratInfoList",
                request.toQueryParams(apiProperties.getServiceKey()),
                new ParameterizedTypeReference<TagoApiResponseWrapper<FlightInfoResponse>>() {}
        );
    }

    public List<AirportInfoResponse> getAirportInfoList() {
        return requestApi(
                "/GetArprtList",
                defaultQueryParams(),
                new ParameterizedTypeReference<TagoApiResponseWrapper<AirportInfoResponse>>() {}
        );
    }

    public List<AirlineInfoResponse> getAirlineInfoList() {
        return requestApi(
                "/GetAirmanList",
                defaultQueryParams(),
                new ParameterizedTypeReference<TagoApiResponseWrapper<AirlineInfoResponse>>() {}
        );
    }

    // ===== Private Methods =====

    private <T> List<T> requestApi(
            String endpoint,
            MultiValueMap<String, String> queryParams,
            ParameterizedTypeReference<TagoApiResponseWrapper<T>> responseType
    ) {
        try {
            URI uri = buildUri(endpoint, queryParams);

            log.info("API URL: {}", uri);

            TagoApiResponseWrapper<T> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(responseType);

            return extractItems(response);
        } catch (Exception e) {
            log.warn("API request failed.", e);
            return Collections.emptyList();
        }
    }

    private URI buildUri(String endpoint, MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromUriString(apiProperties.getUrl())
                .path(endpoint)
                .queryParams(queryParams)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
    }

    private MultiValueMap<String, String> defaultQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceKey", apiProperties.getServiceKey());
        params.add("_type", "json");
        return params;
    }

    private <T> List<T> extractItems(TagoApiResponseWrapper<T> response) {
        if(response == null || response.response() == null) {
            return Collections.emptyList();
        }

        TagoApiResponseWrapper.Header header = response.response().header();
        String resultCode = header.resultCode();

        if(!"00".equals(resultCode)) {
            log.error("API response error: {} - {}}",
                    resultCode,
                    header.resultMessage());
            return Collections.emptyList();
        }

        TagoApiResponseWrapper.Body<T> body = response.response().body();

        if(body == null
                || body.items() == null
                || body.items().item() == null) {
            return Collections.emptyList();
        }

        return body.items().item();
    }
}
