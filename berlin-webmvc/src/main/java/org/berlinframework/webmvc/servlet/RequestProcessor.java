package org.berlinframework.webmvc.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Abhilash Krishnan
 */
public class RequestProcessor {

    public <T> T processJson(String json, Class<T> clazz) {
        T t;
        ObjectMapper mapper = new ObjectMapper();
        try {
            t = mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return t;
    }
}
