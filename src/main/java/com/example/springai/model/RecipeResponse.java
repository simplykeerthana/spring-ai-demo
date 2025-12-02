package com.example.springai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Recipe response model for structured output
 */
public record RecipeResponse(
    String name,
    @JsonProperty("prep_time") String prepTime,
    @JsonProperty("cook_time") String cookTime,
    int servings,
    List<String> ingredients,
    List<String> instructions
) {}
