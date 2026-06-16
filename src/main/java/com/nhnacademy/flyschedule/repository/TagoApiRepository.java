package com.nhnacademy.flyschedule.repository;

import com.nhnacademy.flyschedule.dto.request.FlightInfoRequest;
import com.nhnacademy.flyschedule.dto.resposne.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;

import java.util.List;

public interface TagoApiRepository {
    List<FlightInfoResponse> getFlightInfoList(FlightInfoRequest request);

    List<AirportInfoResponse> getAirportInfoList();

    List<AirlineInfoResponse> getAirlineInfoList();
}
