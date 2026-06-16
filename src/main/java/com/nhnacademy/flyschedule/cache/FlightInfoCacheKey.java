package com.nhnacademy.flyschedule.cache;

public record FlightInfoCacheKey(
        String depAirportId,
        String arrAirportId,
        String departmentDate
) {}