package org.berlinframework.web.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Abhilash Krishnan
 */

public class JsonMapper {

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        T t;
        ObjectMapper mapper = new ObjectMapper();
        try {
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> String objectToJson(T t) {
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
        return json;
    }
}
