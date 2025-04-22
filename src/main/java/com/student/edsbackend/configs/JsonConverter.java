package com.student.edsbackend.configs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading JSON to map", e);
        }
    }

    public static Map<String, String> ensureLangs(Map<String, String> input) {
        Map<String, String> result = new HashMap<>();
        if (input == null) {
            throw new IllegalArgumentException("Input map is null");
        }
    
        List<String> langs = List.of("en", "ru", "kz");
        if (!input.containsKey("en")) {
            throw new IllegalArgumentException("Input map does not contain key 'en'");
        }
        for (String lang : langs) {
            
            result.put(lang, input.getOrDefault(lang, ""));
        }
    
        return result;
    }
}
