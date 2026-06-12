package com.nhnacademy.flyschedule.config;


import com.nhnacademy.flyschedule.mcp.AirlineInfoTool;
import com.nhnacademy.flyschedule.mcp.AirportInfoTool;
import com.nhnacademy.flyschedule.mcp.FlightSearchTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {
    @Primary
    @Bean
    public ChatClient.Builder ollamaAgentChatClientBuilder(
            @Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
            AirportInfoTool airportInfoTool,
            AirlineInfoTool airlineInfoTool,
            FlightSearchTool flightSearchTool
    ) {
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(airportInfoTool, airlineInfoTool, flightSearchTool);
    }

    @Bean
    public ChatClient.Builder geminiAgentChatClientBuilder(
            @Qualifier("googleGenAiChatModel") ChatModel geminiChatModel,
            AirportInfoTool airportInfoTool,
            AirlineInfoTool airlineInfoTool,
            FlightSearchTool flightSearchTool
    ) {
        return ChatClient.builder(geminiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(airportInfoTool,  airlineInfoTool, flightSearchTool);
    }

    @Bean
    public ChatClient.Builder ollamaPlainChatClientBuilder(
            @Qualifier("ollamaChatModel") ChatModel ollamaChatModel
    ) {
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor());
    }

    @Bean
    public ChatClient.Builder geminiPlainChatClientBuilder(
            @Qualifier("googleGenAiChatModel") ChatModel geminiChatModel
    ) {
        return ChatClient.builder(geminiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor());
    }

}
