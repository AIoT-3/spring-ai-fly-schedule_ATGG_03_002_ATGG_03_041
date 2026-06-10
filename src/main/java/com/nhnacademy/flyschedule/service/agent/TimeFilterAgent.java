package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TimeFilterAgent {

    private static final DateTimeFormatter API_DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");


    /**
     * 항공편의 출발 시간을 기준으로 필터링을 수행하는 서비스 클래스.
     *
     * <p>
     * API에서 제공되는 시간 형식(yyyyMMddHHmm)을 {@link LocalTime}으로 변환하여
     * 특정 시간 이전/이후 항공편을 필터링한다.
     * </p>
     *
     * <p>
     * 주요 기능:
     * <ul>
     *     <li>특정 시간 이후 출발 항공편 필터링</li>
     *     <li>특정 시간 이전 출발 항공편 필터링</li>
     * </ul>
     * </p>
     *
     */

    public List<FlightInfoResponse> filterAfterTime(
            List<FlightInfoResponse> flights,
            LocalTime afterTime
    ) {
        return flights.stream()
                .filter(flight ->
                        extractTime(flight.departureTime())
                                .compareTo(afterTime) >= 0
                )
                .toList();
    }

    public List<FlightInfoResponse> filterBeforeTime(
            List<FlightInfoResponse> flights,
            LocalTime beforeTime
    ) {
        return flights.stream()
                .filter(flight ->
                        extractTime(flight.departureTime())
                                .compareTo(beforeTime) <= 0
                )
                .toList();
    }

    private LocalTime extractTime(String departureTime) {
        return LocalTime.from(
                API_DATETIME_FORMAT.parse(departureTime)
        );
    }
}