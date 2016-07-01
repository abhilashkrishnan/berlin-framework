package org.berlinframework.web.context.support;

import org.berlinframework.context.support.GenericApplicationContext;
import org.berlinframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author Abhilash Krishnan
 */
public class GenericWebApplicationContext extends GenericApplicationContext implements WebApplicationContext{
    private ServletContext servletContext;

    public GenericWebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.setClassLoader(this.servletContext.getClassLoader());
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }
}
