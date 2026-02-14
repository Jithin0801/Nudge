package com.jithin.nudge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonObjectMapper {


    ObjectMapper objectMapper = new ObjectMapper();

    public JsonObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String setJsonString(Object obj) throws JsonProcessingException {
        return this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
