package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightGroupingAgent {

    /**
     * 항공편 목록을 항공사명 기준으로 그룹핑합니다.
     *
     * <p>반환되는 Map의 key는 {@link FlightInfoResponse#airlineName()} 값이며,
     * value는 해당 항공사가 운항하는 항공편 목록입니다.</p>
     *
     * @param flights 그룹핑할 항공편 목록. null이 아니어야 하며, 각 항공편의 항공사명도 null이 아니어야 합니다.
     * @return 항공사명을 key로 하고, 해당 항공사의 항공편 목록을 value로 가지는 Map
     */
    public Map<String, List<FlightInfoResponse>> groupByAirline(List<FlightInfoResponse> flights) {
        return flights.stream()
                .collect(Collectors.groupingBy(
                        FlightInfoResponse::airlineName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}
