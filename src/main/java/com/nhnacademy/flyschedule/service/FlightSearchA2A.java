package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.dto.resposne.FlightInfoResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FlightSearchA2A {
    private final ChatClient chatClient;

    public FlightSearchA2A(
            @Qualifier("geminiAgentChatClientBuilder") ChatClient.Builder geminiChatClientBuilder
    ) {
        this.chatClient = geminiChatClientBuilder.build();
    }

    public Map<String, List<FlightInfoResponse>> search(String prompt) {

        return chatClient.prompt()
                .system("tool만 활용해서 직접 데이터 생성금지")
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<Map<String, List<FlightInfoResponse>>>() {});
    }

}
