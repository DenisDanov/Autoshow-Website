package com.example.demo.dbData;

import com.example.demo.dbData.orderCarService.CarOrder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class CarOrderListConverter implements AttributeConverter<List<CarOrder>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<CarOrder> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle the exception based on your requirements
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CarOrder> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>(); // Return an empty list
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<CarOrder>>() {});
        } catch (JsonParseException e) {
            // Handle the exception based on your requirements
            e.printStackTrace(); // Log the exception or perform custom handling
            return new ArrayList<>(); // Return an empty list in case of parsing failure
        } catch (JsonProcessingException e) {
            // Handle other JSON processing exceptions
            e.printStackTrace(); // Log the exception
            return new ArrayList<>(); // Return an empty list in case of other processing failures
        }
    }
}
