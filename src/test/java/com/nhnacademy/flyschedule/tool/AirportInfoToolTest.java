package com.nhnacademy.flyschedule.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AirportInfoToolTest {

    @Autowired
    private ChatClient.Builder ollamaChatClientBuilder;

    @Test
    void getAirportCode() {
        ChatClient ollamaChatClient = ollamaChatClientBuilder.build();

        String content = ollamaChatClient
                .prompt()
                .user("항공사 목록 불러와줘 AirportInfoTool 사용해줘 json형식으로 변환해줘")
                .call()
                .content();

        log.info(content);
    }
}