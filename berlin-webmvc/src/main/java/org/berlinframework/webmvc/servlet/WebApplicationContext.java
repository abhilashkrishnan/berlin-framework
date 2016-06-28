package org.berlinframework.webmvc.servlet;

import org.berlinframework.beans.factory.ApplicationContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ACER on 28-06-2016.
 */
public class WebApplicationContext extends ApplicationContext {
    private Map<String, Object> controllers = new HashMap<>();

    public Map<String, Object> getControllers() {
        return this.controllers;
    }

    public Object getController(String path) {
        return this.controllers.get(path);
    }
}
