package com.nhnacademy.flyschedule.service.ai;

import com.nhnacademy.flyschedule.dto.ModelType;

import com.nhnacademy.flyschedule.dto.response.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FlightSearchA2A {

    private final ChatClient geminiClient;
    private final ChatClient ollamaClient;

    public FlightSearchA2A(
            @Qualifier("geminiAgentChatClientBuilder") ChatClient.Builder geminiBuilder,
            @Qualifier("ollamaAgentChatClientBuilder") ChatClient.Builder ollamaBuilder
    ) {
        this.geminiClient = geminiBuilder.build();
        this.ollamaClient = ollamaBuilder.build();
    }

    public Map<String, List<FlightInfoResponse>> search(String prompt) {



        return ollamaClient.prompt()
                .system("tool만 활용해서 직접 데이터 생성금지")
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<Map<String, List<FlightInfoResponse>>>() {});
    }

    public Map<String, List<FlightInfoResponse>> search(String prompt, ModelType modelType) {

        ModelType resolved = ModelType.defaultIfNull(modelType);

        ChatClient client = switch (resolved) {
            case GEMINI -> geminiClient;
            case OLLAMA -> ollamaClient;
        };

        return client.prompt()
                .system("tool만 활용해서 직접 데이터 생성금지")
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<Map<String, List<FlightInfoResponse>>>() {});
    }
}