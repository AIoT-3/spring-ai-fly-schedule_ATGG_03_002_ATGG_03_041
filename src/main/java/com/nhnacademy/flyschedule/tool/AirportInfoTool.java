package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.dto.resposne.AirportInfoResponse;
import com.nhnacademy.flyschedule.service.agent.AirPortCodeAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirportInfoTool {
    private final AirPortCodeAgent airPortCodeAgent;

    @Tool(
            description = "전제 공항 목록을 조회합니다. " +
                    "국내 모든 공항의 코드와 이름을 반환합니다."
    )
    public List<AirportInfoResponse> getAirportInfo() {

        return airPortCodeAgent.getAirportInfoList();
    }

    @Tool(
            description = "공항 이름으로 공항 코드를 조회합니다." +
                    "공항 이름을 입력하면 해당공항의 코드를 반환합니다. "
    )
    public String getAirportCode(
            @ToolParam(description = "공항 이름 (예: 광주, 김포, 제주)") String airportName) {

        log.info("MCP Tool 호출: getAirportCode(airportName={})", airportName);

        return airPortCodeAgent.getAirportCode(airportName);
    }

}
