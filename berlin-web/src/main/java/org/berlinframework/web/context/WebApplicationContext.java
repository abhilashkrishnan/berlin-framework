package org.berlinframework.web.context;

import org.berlinframework.beans.factory.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author Abhilash Krishnan
 */
public interface WebApplicationContext extends ApplicationContext {

    ServletContext getServletContext();
}
