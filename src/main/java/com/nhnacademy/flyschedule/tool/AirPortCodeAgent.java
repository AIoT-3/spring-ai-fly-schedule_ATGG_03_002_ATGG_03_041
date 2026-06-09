package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.service.TagoApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirPortCodeAgent {
    private final TagoApiService tagoApiService;
    private final Map<String, String> airportMap = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        List<AirportInfoResponse> airportInfoList =
                tagoApiService.getAirportInfoList();

        Object first = airportInfoList.getFirst();

        log.info("class={}", first.getClass());

        log.info("공항 정보 {} 건 로딩 완료", airportMap.size());
    }

    /**
     * 공항명 또는 공항코드를 공항코드로 변환합니다.
     * <p>
     * 지원 예시:
     * - 인천국제공항 -> ICN
     * - 인천 -> ICN
     * - ICN -> ICN
     *
     * @param airportName 공항명 또는 공항코드
     * @return 공항코드
     */

    public String getAirportCode(String airportName) {
        if (airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요.");
        }

        String normalized = airportName.trim();

        // 공학코드 입력한경우 그대로 리턴
        if (airportMap.containsValue(normalized)) {
            return normalized.toUpperCase();
        }


        String code = airportMap.get(normalized);
        if (code != null) {
            return code;
        }

        // 부분 검색도 허용
        return airportMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().contains(normalized))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("알 수 없는 공항: {}", airportName);
                    return new IllegalArgumentException(
                            "알 수 없는 공항입니다: " + airportName
                    );
                });


    }

}
