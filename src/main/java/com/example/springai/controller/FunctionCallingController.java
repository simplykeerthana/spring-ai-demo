package com.example.springai.controller;

import com.example.springai.service.WeatherService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Demo 3: Function Calling
 * Demonstrates AI model calling Java methods to get real-time data
 * Note: Function calling requires OpenAI or other providers that support it.
 * Ollama has limited function calling support, so this demo shows a simpler approach.
 */
@RestController
@RequestMapping("/api/functions")
public class FunctionCallingController {

    private final ChatModel chatModel;
    private final WeatherService weatherService;

    public FunctionCallingController(ChatModel chatModel, WeatherService weatherService) {
        this.chatModel = chatModel;
        this.weatherService = weatherService;
    }

    /**
     * AI decides when to call weather function
     * Example: GET /api/functions/weather?question=What's the weather like in Tokyo?
     */
    @GetMapping("/weather")
    public Map<String, Object> askWeather(@RequestParam String question) {
        // For this demo, we'll use a simpler approach that works with all providers
        // Extract city name from the question and call the weather service
        String city = extractCityFromQuestion(question);
        
        String weatherInfo = "";
        if (city != null && !city.isEmpty()) {
            weatherInfo = weatherService.getCurrentWeather(
                new WeatherService.WeatherRequest(city)
            );
        }

        // Create a prompt with the weather information as context
        String prompt = weatherInfo.isEmpty() 
            ? question 
            : String.format("Context: %s\n\nQuestion: %s\n\nAnswer based on the context above.", 
                weatherInfo, question);

        String response = chatModel.call(prompt);

        return Map.of(
            "question", question,
            "answer", response,
            "weatherData", weatherInfo
        );
    }

    /**
     * AI with multiple function options
     * Example: GET /api/functions/assistant?question=Calculate the tip for a $50 bill at 20%
     */
    @GetMapping("/assistant")
    public Map<String, Object> smartAssistant(@RequestParam String question) {
        // Check if it's a calculation question
        if (question.toLowerCase().contains("calculate") || 
            question.toLowerCase().contains("tip") ||
            question.matches(".*\\d+.*[+\\-*/].*\\d+.*")) {
            
            // Try to extract and calculate
            String calculation = extractCalculation(question);
            if (!calculation.isEmpty()) {
                String result = calculate(new CalculateRequest(calculation));
                String prompt = String.format(
                    "The calculation result is: %s\n\nQuestion: %s\n\nProvide a natural response using this result.",
                    result, question
                );
                String response = chatModel.call(prompt);
                return Map.of(
                    "question", question,
                    "answer", response,
                    "calculation", result
                );
            }
        }
        
        // Check if it's a weather question
        if (question.toLowerCase().contains("weather") || 
            question.toLowerCase().contains("temperature") ||
            question.toLowerCase().contains("sunny") ||
            question.toLowerCase().contains("rain")) {
            
            String city = extractCityFromQuestion(question);
            if (city != null && !city.isEmpty()) {
                return askWeather(question);
            }
        }

        // Default: just answer the question
        String response = chatModel.call(question);
        return Map.of(
            "question", question,
            "answer", response
        );
    }
    
    /**
     * Extract city name from question
     */
    private String extractCityFromQuestion(String question) {
        String[] commonCities = {"Tokyo", "London", "Paris", "New York", "San Francisco", "Sydney"};
        String lowerQuestion = question.toLowerCase();
        
        for (String city : commonCities) {
            if (lowerQuestion.contains(city.toLowerCase())) {
                return city;
            }
        }
        return "";
    }
    
    /**
     * Extract calculation from question
     */
    private String extractCalculation(String question) {
        // Simple extraction for tip calculation
        if (question.toLowerCase().contains("tip")) {
            // Try to extract numbers
            String[] parts = question.split("\\s+");
            double amount = 0;
            double percentage = 0;
            
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i].replaceAll("[^0-9.]", "");
                if (!part.isEmpty()) {
                    double num = Double.parseDouble(part);
                    if (amount == 0) {
                        amount = num;
                    } else if (percentage == 0) {
                        percentage = num;
                    }
                }
            }
            
            if (amount > 0 && percentage > 0) {
                return amount + "*" + (percentage / 100);
            }
        }
        
        // Try to find arithmetic expression
        if (question.matches(".*\\d+\\s*[+\\-*/]\\s*\\d+.*")) {
            return question.replaceAll("[^0-9+\\-*/.]", "");
        }
        
        return "";
    }

    // Simple calculator function
    public record CalculateRequest(String expression) {}
    
    public String calculate(CalculateRequest request) {
        // Simple calculator (in real app, use a proper expression parser)
        try {
            String expr = request.expression()
                .replaceAll("\\s+", "")
                .toLowerCase();
            
            // Handle basic operations
            if (expr.contains("+")) {
                String[] parts = expr.split("\\+");
                double result = Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
                return String.valueOf(result);
            } else if (expr.contains("-")) {
                String[] parts = expr.split("-");
                double result = Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
                return String.valueOf(result);
            } else if (expr.contains("*")) {
                String[] parts = expr.split("\\*");
                double result = Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
                return String.valueOf(result);
            } else if (expr.contains("/")) {
                String[] parts = expr.split("/");
                double result = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                return String.valueOf(result);
            }
            
            return "Could not parse expression: " + request.expression();
        } catch (Exception e) {
            return "Error calculating: " + e.getMessage();
        }
    }
}
