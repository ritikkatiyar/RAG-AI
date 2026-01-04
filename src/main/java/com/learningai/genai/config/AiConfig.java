package com.learningai.genai.config;

import com.learningai.genai.model.WeatherRequest;
import com.learningai.genai.service.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel){
        return ChatClient.builder(chatModel).build();
    }

//    @Bean
//    @Description("Get the weather in location")
//    Function<WeatherRequest, WeatherResponse> currentWeather() {
//        return new WeatherService();
//    }

    @Bean
    public ToolCallback weatherTool(WeatherService weatherService) {

        return FunctionToolCallback
                .builder(
                        "currentWeather",
                        weatherService::currentWeather
                )
                .description("Get the current weather for a city")
                .inputType(WeatherRequest.class)
                .build();
    }
}
