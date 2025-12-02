package com.example.springai.controller;

import com.example.springai.model.BookRecommendation;
import com.example.springai.model.RecipeResponse;
import com.example.springai.model.WeatherResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Demo 2: Structured Output
 * Demonstrates converting AI responses into Java POJOs
 */
@RestController
@RequestMapping("/api/structured")
public class StructuredOutputController {

    private final ChatModel chatModel;

    public StructuredOutputController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Get weather information as structured data
     * Example: GET /api/structured/weather?city=San Francisco
     */
    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam String city) {
        BeanOutputConverter<WeatherResponse> outputConverter = 
            new BeanOutputConverter<>(WeatherResponse.class);

        String format = outputConverter.getFormat();
        
        String template = """
            Provide current weather information for {city}.
            Include temperature, conditions, humidity, and wind speed.
            Make realistic estimates based on typical weather patterns.
            
            {format}
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(
            Map.of("city", city, "format", format)
        );

        String response = chatModel.call(prompt).getResult().getOutput().getContent();
        return outputConverter.convert(response);
    }

    /**
     * Get book recommendations as structured data
     * Example: GET /api/structured/books?genre=science fiction&count=3
     */
    @GetMapping("/books")
    public BookRecommendation getBookRecommendations(
            @RequestParam String genre,
            @RequestParam(defaultValue = "3") int count) {
        
        BeanOutputConverter<BookRecommendation> outputConverter = 
            new BeanOutputConverter<>(BookRecommendation.class);

        String format = outputConverter.getFormat();
        
        String template = """
            Recommend {count} popular {genre} books.
            Include title, author, year published, and a brief description for each.
            
            {format}
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(
            Map.of("genre", genre, "count", String.valueOf(count), "format", format)
        );

        String response = chatModel.call(prompt).getResult().getOutput().getContent();
        return outputConverter.convert(response);
    }

    /**
     * Get recipe with ingredients and steps
     * Example: GET /api/structured/recipe?dish=pasta carbonara
     */
    @GetMapping("/recipe")
    public RecipeResponse getRecipe(@RequestParam String dish) {
        BeanOutputConverter<RecipeResponse> outputConverter = 
            new BeanOutputConverter<>(RecipeResponse.class);

        String format = outputConverter.getFormat();
        
        String template = """
            Provide a detailed recipe for {dish}.
            Include ingredients with measurements and step-by-step cooking instructions.
            Also include prep time, cook time, and servings.
            
            {format}
            """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(
            Map.of("dish", dish, "format", format)
        );

        String response = chatModel.call(prompt).getResult().getOutput().getContent();
        return outputConverter.convert(response);
    }
}
