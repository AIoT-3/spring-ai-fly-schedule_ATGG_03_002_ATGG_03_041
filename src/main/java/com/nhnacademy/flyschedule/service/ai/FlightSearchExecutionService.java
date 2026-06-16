package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.FlightSearchCommand;
import com.nhnacademy.flyschedule.dto.FlightSearchCriteria;
import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightSearchAgent;
import com.nhnacademy.flyschedule.service.agent.FlightSearchConditionAgent;
import com.nhnacademy.flyschedule.service.agent.FlightSearchResultFilterAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 항공편 검색의 공통 실행 파이프라인을 담당하는 Service입니다.
 *
 * <p>Coordinator 방식과 MCP Tool Orchestration 방식은 입력 생성 방법만 다르고,
 * 검색 조건 정리, 항공편 조회, 결과 필터링은 동일한 정책을 공유해야 합니다.
 * 이 Service는 그 공통 실행 흐름을 하나로 모읍니다.</p>
 *
 * <p>구현 예정 흐름:</p>
 * <ol>
 *     <li>{@link FlightSearchCommand}를 {@link FlightSearchConditionAgent}로 정규화 및 검증한다.</li>
 *     <li>{@link FlightSearchAgent}로 항공편을 조회한다.</li>
 *     <li>{@link FlightSearchResultFilterAgent}로 시간, 가격, limit 필터를 적용한다.</li>
 *     <li>항공사별로 그룹핑된 최종 검색 결과를 반환한다.</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
public class FlightSearchExecutionService {
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final FlightSearchConditionAgent flightSearchConditionAgent;
    private final FlightSearchAgent flightSearchAgent;
    private final FlightSearchResultFilterAgent flightSearchResultFilterAgent;

    /**
     * 원시 검색 명령을 받아 항공편 검색 공통 실행 흐름을 수행합니다.
     *
     * @param command Coordinator 또는 Orchestrator가 생성한 원시 검색 명령
     * @return 필터링과 그룹핑이 끝난 항공사별 검색 결과
     */
    public Map<String, List<FlightInfoResponse>> search(FlightSearchCommand command) {
        // 1. LLM에서 받은 파라미터를 보정 및 검증
        FlightSearchCriteria criteria =
                flightSearchConditionAgent.normalizeAndValidate(command);
        // 2. 검증 조건에 맞는 원본 항공편 목록 조회
        List<FlightInfoResponse> flights =
                searchFlights(criteria);
        // 3. 공통 후처리 정책 적용
        return flightSearchResultFilterAgent.apply(flights, criteria);
    }

    /**
     * 정규화된 검색 조건으로 실제 항공편 조회를 수행합니다.
     *
     * @param criteria 정규화와 검증이 완료된 검색 조건
     * @return 원본 항공편 검색 결과
     */
    private List<FlightInfoResponse> searchFlights(FlightSearchCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("항공편 검색 조건은 필수입니다.");
        }

        return flightSearchAgent.search(
                criteria.departure(),
                criteria.arrival(),
                criteria.date().format(API_DATE_FORMAT)
        );
    }
}
