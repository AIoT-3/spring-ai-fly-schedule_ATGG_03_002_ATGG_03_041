package com.nhnacademy.flyschedule.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nhnacademy.flyschedule.cache.FlightInfoCacheKey;
import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.repository.TagoApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagoApiServiceTest {
    @Mock
    private TagoApiRepository tagoApiRepository;

    private Cache<FlightInfoCacheKey, List<FlightInfoResponse>> flightInfoCache;
    private TagoApiService tagoApiService;

    @BeforeEach
    void setUp() {
        flightInfoCache = Caffeine.newBuilder()
                .maximumSize(100)
                .recordStats()
                .build();
        tagoApiService = new TagoApiService(tagoApiRepository, flightInfoCache);
    }

    @Test
    void getFlightInfoList_duplicateRequest_callsRepositoryOnlyOnceAndReturnsCachedResponse() {
        FlightInfoRequest request = new FlightInfoRequest(
                "NAARKJJ",
                "NAARKPC",
                "20260610"
        );
        List<FlightInfoResponse> repositoryFlights = List.of(
                createFlight("OZ1001")
        );

        when(tagoApiRepository.getFlightInfoList(request))
                .thenReturn(repositoryFlights);

        List<FlightInfoResponse> firstResult = tagoApiService.getFlightInfoList(request);
        List<FlightInfoResponse> secondResult = tagoApiService.getFlightInfoList(request);

        assertSame(repositoryFlights, firstResult);
        assertSame(repositoryFlights, secondResult);
        assertEquals("OZ1001", secondResult.get(0).flightId());
        assertEquals(1, flightInfoCache.stats().missCount());
        assertEquals(1, flightInfoCache.stats().hitCount());
        verify(tagoApiRepository, times(1)).getFlightInfoList(request);
        verifyNoMoreInteractions(tagoApiRepository);
    }

    @Test
    void getFlightInfoList_differentRequest_usesDifferentCacheKey() {
        FlightInfoRequest firstRequest = new FlightInfoRequest(
                "NAARKJJ",
                "NAARKPC",
                "20260610"
        );
        FlightInfoRequest secondRequest = new FlightInfoRequest(
                "NAARKJJ",
                "NAARKPC",
                "20260611"
        );
        List<FlightInfoResponse> firstFlights = List.of(createFlight("OZ1001"));
        List<FlightInfoResponse> secondFlights = List.of(createFlight("OZ1002"));

        when(tagoApiRepository.getFlightInfoList(firstRequest))
                .thenReturn(firstFlights);
        when(tagoApiRepository.getFlightInfoList(secondRequest))
                .thenReturn(secondFlights);

        List<FlightInfoResponse> firstResult = tagoApiService.getFlightInfoList(firstRequest);
        List<FlightInfoResponse> secondResult = tagoApiService.getFlightInfoList(secondRequest);

        assertSame(firstFlights, firstResult);
        assertSame(secondFlights, secondResult);
        assertEquals("OZ1001", firstResult.get(0).flightId());
        assertEquals("OZ1002", secondResult.get(0).flightId());
        assertEquals(2, flightInfoCache.stats().missCount());
        assertEquals(0, flightInfoCache.stats().hitCount());
        verify(tagoApiRepository, times(1)).getFlightInfoList(firstRequest);
        verify(tagoApiRepository, times(1)).getFlightInfoList(secondRequest);
        verifyNoMoreInteractions(tagoApiRepository);
    }

    @Test
    void getAirportInfoList_delegatesToRepository() {
        List<AirportInfoResponse> airports = List.of(
                new AirportInfoResponse("광주", "NAARKJJ"),
                new AirportInfoResponse("제주", "NAARKPC")
        );

        when(tagoApiRepository.getAirportInfoList())
                .thenReturn(airports);

        List<AirportInfoResponse> result = tagoApiService.getAirportInfoList();

        assertSame(airports, result);
        assertEquals("NAARKJJ", result.get(0).airportId());
        verify(tagoApiRepository, times(1)).getAirportInfoList();
        verifyNoMoreInteractions(tagoApiRepository);
    }

    @Test
    void getAirlineInfoList_delegatesToRepository() {
        List<AirlineInfoResponse> airlines = List.of(
                new AirlineInfoResponse("아시아나항공", "AAR"),
                new AirlineInfoResponse("대한항공", "KAL")
        );

        when(tagoApiRepository.getAirlineInfoList())
                .thenReturn(airlines);

        List<AirlineInfoResponse> result = tagoApiService.getAirlineInfoList();

        assertSame(airlines, result);
        assertEquals("AAR", result.get(0).airlineId());
        verify(tagoApiRepository, times(1)).getAirlineInfoList();
        verifyNoMoreInteractions(tagoApiRepository);
    }

    private FlightInfoResponse createFlight(String flightId) {
        return new FlightInfoResponse(
                flightId,
                "아시아나항공",
                "202606101000",
                "202606101100",
                50000,
                0,
                "광주",
                "제주"
        );
    }
}
