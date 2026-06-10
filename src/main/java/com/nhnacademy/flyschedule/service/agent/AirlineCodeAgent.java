package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineCodeAgent {
    private final TagoApiService tagoApiService;
    private final Map<String, String> airlineCodeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<AirlineInfoResponse> airlineInfoList = tagoApiService.getAirlineInfoList();
        airlineInfoList
                .forEach(airlineInfo ->
                        airlineCodeMap.put(airlineInfo.airlineName(), airlineInfo.airlineId())
                );
        log.info("항공사 정보 로딩완료  {}건", airlineCodeMap.size());
    }

    public String getAirlineCode(String airlineName) {
        if (airlineName == null || airlineName.isBlank()) {
            throw new IllegalArgumentException("항공사 이름이 비어있습니다.");
        }

        String result = airlineCodeMap.get(airlineName.trim());

        if(result == null) {
            throw new IllegalArgumentException("해당하는 항공사 정보가 없습니다.");
        }

        return result;
    }
}
