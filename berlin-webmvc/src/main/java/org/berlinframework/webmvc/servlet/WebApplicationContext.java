package org.berlinframework.webmvc.servlet;

import org.berlinframework.beans.factory.ApplicationContext;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhilash Krishnan
 */
public class WebApplicationContext extends ApplicationContext {
    private ServletContext servletContext;

    public WebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.setClassLoader(this.servletContext.getClassLoader());
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
