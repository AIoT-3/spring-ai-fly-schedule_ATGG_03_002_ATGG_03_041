package com.nhnacademy.flyschedule.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nhnacademy.flyschedule.cache.FlightInfoCacheKey;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {
    @Bean
    public Cache<FlightInfoCacheKey, List<FlightInfoResponse>> flightInfoCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(Duration.ofMinutes(30))
                .recordStats()
                .build();
    }

}