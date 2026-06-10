package com.nhnacademy.flyschedule.mcp;

import com.nhnacademy.flyschedule.service.agent.AirlineCodeAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AirlineInfoTool {
    private final AirlineCodeAgent airlineCodeAgent;

    @Tool(
            description = """
                    항공사 한글 이름을 입력받아 항공사 ID(airlineId)를 반환합니다.
                    airlineId는 3자리 영문 대문자로 된 해당 항공사의 IATA 코드입니다.
                    입력값은 '대한항공', '제주항공', '진에어'처럼 항공사 이름만 전달해야 합니다.
                    """
    )
    public String getAirlineId(
            @ToolParam(description = "항공사 한글 이름. 예: 대한항공, 제주항공, 진에어") String airlineName
    ) {
        return airlineCodeAgent.getAirlineCode(airlineName);
    }
}
