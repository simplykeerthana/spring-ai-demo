package com.example.springai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Weather response model for structured output
 */
public record WeatherResponse(
    String city,
    double temperature,
    String conditions,
    int humidity,
    @JsonProperty("wind_speed") double windSpeed,
    String unit
) {}
