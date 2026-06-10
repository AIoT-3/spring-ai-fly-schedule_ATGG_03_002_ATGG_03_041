package com.nhnacademy.flyschedule.service.agent;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class PriceFilterAgent {

    /**
     * 항공편 목록에서 일반석 요금이 지정된 가격 범위에 포함되는 항공편만 반환합니다.
     *
     * <p>{@code minPrice} 또는 {@code maxPrice}가 {@code null}이면 해당 하한/상한 조건은 적용하지 않습니다.
     * 요금 정보가 없거나 0인 항공편은 제외합니다.</p>
     *
     * @param flights 필터링할 항공편 목록. {@code null}일 경우, {@code Empty List}를 반환합니다.
     * @param minPrice 포함할 최소 가격.
     * @param maxPrice 포함할 최대 가격.
     * @return 가격 범위 조건을 만족하는 항공편 목록
     */
    public List<FlightInfoResponse> filterByPriceRange(
            List<FlightInfoResponse> flights,
            Integer minPrice,
            Integer maxPrice
    ) {
        if(flights == null || flights.isEmpty()) {
            return List.of();
        }

        return flights.stream()
                .filter(flight ->
                        isInRange(flight.economyCharge(), minPrice, maxPrice))
                .toList();
    }

    /**
     * 항공편 목록에서 가장 저렴한 일반석 요금을 가진 항공편을 찾습니다.
     *
     * <p>요금 정보가 없거나 0인 항공편은 비교 대상에서 제외합니다.</p>
     *
     * @param flights 조회할 항공편 목록. null이 아니어야 합니다.
     * @return 가장 저렴한 항공편. 유효한 요금 정보가 없으면 null
     */
    public FlightInfoResponse findCheapestPrice(List<FlightInfoResponse> flights) {
        return flights.stream()
                .filter(f -> isValidPrice(f.economyCharge()))
                .min(Comparator.comparing(FlightInfoResponse::economyCharge))
                .orElse(null);
    }

    /**
     * 항공편 목록에서 가장 비싼 일반석 요금을 가진 항공편을 찾습니다.
     *
     * <p>요금 정보가 없거나 0인 항공편은 비교 대상에서 제외합니다.</p>
     *
     * @param flights 조회할 항공편 목록. null이 아니어야 합니다.
     * @return 가장 비싼 항공편. 유효한 요금 정보가 없으면 null
     */
    public FlightInfoResponse findMostExpensivePrice(List<FlightInfoResponse> flights) {
        return flights.stream()
                .filter(f -> isValidPrice(f.economyCharge()))
                .max(Comparator.comparing(FlightInfoResponse::economyCharge))
                .orElse(null);
    }

    /**
     * 항공편 목록의 유효한 일반석 요금 평균을 계산합니다.
     *
     * <p>요금 정보가 없거나 0인 항공편은 평균 계산에서 제외합니다.</p>
     *
     * @param flights 평균을 계산할 항공편 목록. null이 아니어야 합니다.
     * @return 유효한 요금의 평균. 계산할 요금이 없으면 0.0
     */
    public double calculateAveragePrice(List<FlightInfoResponse> flights) {
        return flights.stream()
                .filter(f -> isValidPrice(f.economyCharge()))
                .mapToInt(FlightInfoResponse::economyCharge)
                .average()
                .orElse(0.0);
    }

     // ===== Private Methods =====

    private boolean isValidPrice(Integer price) {
        return price != null && price > 0;
    }

    private boolean isInRange(Integer price, Integer minPrice, Integer maxPrice) {
        return isValidPrice(price)
                && (minPrice == null || price >= minPrice)
                && (maxPrice == null || price <= maxPrice);
    }
}
