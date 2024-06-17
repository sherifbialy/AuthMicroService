package com.sumerge.auth.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public final class Utilities {

    private static ObjectMapper mapper;

    private Utilities() {
    }

    public static <T> T getObject(Object event, Class<T> tClass) {
        return getObjectMapper().convertValue(event, tClass);
    }

    public static ObjectMapper getObjectMapper() {
        if(mapper == null) {
            mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        return mapper;
    }

    public static ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return mapper;
    }

    public static String mapToJson(Object obj) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(obj);
    }

    public static <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return getObjectMapper().readValue(json, clazz);
    }

}
