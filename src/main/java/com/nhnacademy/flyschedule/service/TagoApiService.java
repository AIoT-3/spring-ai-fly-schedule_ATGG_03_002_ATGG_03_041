package com.nhnacademy.flyschedule.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.nhnacademy.flyschedule.cache.FlightInfoCacheKey;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.response.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.response.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.repository.TagoApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagoApiService {
    private final TagoApiRepository tagoApiRepository;
    private final Cache<FlightInfoCacheKey, List<FlightInfoResponse>> flightInfoCache;

    public List<FlightInfoResponse> getFlightInfoList(FlightInfoRequest request) {
        FlightInfoCacheKey key = new FlightInfoCacheKey(
                request.depAirportId(),
                request.arrAirportId(),
                request.departmentDate()
        );

        return flightInfoCache.get(
                key,
                ignored -> tagoApiRepository.getFlightInfoList(request)
        );
    }

    public List<AirportInfoResponse> getAirportInfoList() {
        return tagoApiRepository.getAirportInfoList();
    }

    public List<AirlineInfoResponse> getAirlineInfoList() {
        return tagoApiRepository.getAirlineInfoList();
    }
}
