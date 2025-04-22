package com.student.edsbackend.configs;

import java.util.HashMap;
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
        result.put("en", input != null ? input.getOrDefault("en", "") : "");
        result.put("ru", input != null ? input.getOrDefault("ru", "") : "");
        result.put("kz", input != null ? input.getOrDefault("kz", "") : "");
        return result;
    }
}
