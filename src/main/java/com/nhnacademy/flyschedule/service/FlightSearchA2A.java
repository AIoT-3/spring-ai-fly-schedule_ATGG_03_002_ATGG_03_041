package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.dto.FlightSearchExtractResult;
import com.nhnacademy.flyschedule.dto.ModelType;
import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import com.nhnacademy.flyschedule.service.agent.FlightSearchAgent;
import com.nhnacademy.flyschedule.service.agent.PriceFilterAgent;
import com.nhnacademy.flyschedule.service.agent.TimeFilterAgent;
import com.nhnacademy.flyschedule.service.ai.FlightSearchExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlightSearchA2A {
    private ChatClient chatClient;

    public FlightSearchA2A(@Qualifier("geminiAgentChatClientBuilder") ChatClient.Builder geminiChatClientBuilder) {
        chatClient = geminiChatClientBuilder.build();
    }

    public List<FlightInfoResponse> search(String prompt) {

        return chatClient.prompt()
                .system("tool만 활용해서 직접 데이터 생성금지")
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<FlightInfoResponse>>() {});
    }

}
