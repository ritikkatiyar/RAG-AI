package com.learningai.genai.service;

import com.learningai.genai.constants.Unit;
import com.learningai.genai.model.WeatherRequest;
import com.learningai.genai.model.WeatherResponse;
import org.springframework.stereotype.Component;

@Component
public class WeatherService {

    public WeatherResponse currentWeather(WeatherRequest request) {
        return new WeatherResponse(30.0, Unit.C);
    }
}

