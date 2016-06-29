package org.berlinframework.webmvc.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Abhilash Krishnan
 */
public class ResponseProcessor {

    public <T> String processObject(T t) {
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
