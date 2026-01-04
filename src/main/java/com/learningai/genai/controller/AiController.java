package com.learningai.genai.controller;

import com.learningai.genai.service.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AiController {

    private final ChatClient chatClient;
    private final ToolCallback weatherTool;

    public AiController(ChatClient chatClient,ToolCallback weatherTool) {
        this.chatClient = chatClient;
        this.weatherTool=weatherTool;
    }

    @GetMapping("/")
    public List<String> findPopularPlayers(@RequestParam String sports) {

        PromptTemplate template =
                new PromptTemplate("List 5 most popular personalities in {sports}.");

        ListOutputConverter converter=new ListOutputConverter(new DefaultConversionService());

        Prompt prompt = template.create(Map.of("sports", sports));
        ChatResponse chatResponse=chatClient.prompt(prompt)
                .call()
                .chatResponse();
        try{
            return converter.convert(chatResponse.getResult().getOutput().getText());
        } catch (Exception e) {
            return List.of("Ai service not availble to cater request");
        }

    }
    @GetMapping("/weather")
    public String getWeather(@RequestParam(value = "message",
            defaultValue = "what is the weather of gurugram")String message){

        ChatResponse chatResponse=chatClient
                .prompt()
                .user(message)
                .toolCallbacks(weatherTool)
                .call().chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

}

