package com.example.springai.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather service that provides weather data
 * In a real application, this would call an actual weather API
 */
@Service
public class WeatherService {

    // Mock weather data
    private final Map<String, WeatherData> weatherDatabase = new HashMap<>();

    public WeatherService() {
        // Initialize with some mock data
        weatherDatabase.put("london", new WeatherData("London", 15.5, "Cloudy", 75));
        weatherDatabase.put("tokyo", new WeatherData("Tokyo", 22.0, "Sunny", 60));
        weatherDatabase.put("new york", new WeatherData("New York", 18.0, "Partly Cloudy", 65));
        weatherDatabase.put("san francisco", new WeatherData("San Francisco", 16.0, "Foggy", 80));
        weatherDatabase.put("paris", new WeatherData("Paris", 17.0, "Rainy", 85));
        weatherDatabase.put("sydney", new WeatherData("Sydney", 25.0, "Sunny", 55));
    }

    /**
     * Get current weather for a city
     * This method will be called by the AI model when needed
     */
    public String getCurrentWeather(WeatherRequest request) {
        String city = request.city().toLowerCase();
        
        WeatherData data = weatherDatabase.getOrDefault(city, 
            new WeatherData(request.city(), 20.0, "Unknown", 50));

        return String.format(
            "The current weather in %s is %s with a temperature of %.1f°C and %d%% humidity.",
            data.city(), data.conditions(), data.temperature(), data.humidity()
        );
    }

    /**
     * Request model for weather function
     */
    public record WeatherRequest(String city) {}

    /**
     * Internal weather data model
     */
    private record WeatherData(
        String city,
        double temperature,
        String conditions,
        int humidity
    ) {}
}
