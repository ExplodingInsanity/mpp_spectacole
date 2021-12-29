package com.example.domain.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.lang.reflect.Type;
import java.util.List;

@Convert
public class Converter implements AttributeConverter<List<Integer>, String> {

    Type listType = new TypeToken<List<Integer>>() {}.getType();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        Gson gson = new Gson();

        return gson.toJson(attribute,listType);
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        Gson gson = new Gson();
        return gson.fromJson(dbData,listType);
    }
}
