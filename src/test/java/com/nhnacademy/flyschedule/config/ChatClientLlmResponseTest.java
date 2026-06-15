package com.nhnacademy.flyschedule.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "RUN_LLM_TESTS", matches = "true")
class ChatClientLlmResponseTest {
    private final ChatClient ollamaChatClient;
    private final ChatClient geminiChatClient;


    ChatClientLlmResponseTest(
            @Qualifier("ollamaAgentChatClientBuilder") ChatClient.Builder ollamachatClientBuilder,
            @Qualifier("geminiAgentChatClientBuilder") ChatClient.Builder geminiChatClientBuilder
    ) {
        this.ollamaChatClient = ollamachatClientBuilder.build();
        this.geminiChatClient = geminiChatClientBuilder.build();
    }

    @Test
    void ollamaResponseLogTest() {
        llmResponse(ollamaChatClient);
    }

    @Test
    void geminiResponseLogTest() {
        llmResponse(geminiChatClient);
    }

    private void llmResponse(ChatClient chatClient) {
        String userText = "내일 광주에서 제주로 가는 항공편 조회해서 보여줘.";

        String response = chatClient.prompt()
                .user(userText)
                .call()
                .content();

        log.info("LLM user text: {}", userText);
        log.info("LLM response: {}", response);

        assertNotNull(response);
        assertFalse(response.isBlank());
    }
}
