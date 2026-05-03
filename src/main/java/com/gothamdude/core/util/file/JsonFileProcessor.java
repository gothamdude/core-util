package com.gothamdude.core.util.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;


public class JsonFileProcessor {
    // Reusable, thread-safe instance
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonFileProcessor() { } // Prevent instantiation

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error reading from JSON", e);
        }
    }
}
