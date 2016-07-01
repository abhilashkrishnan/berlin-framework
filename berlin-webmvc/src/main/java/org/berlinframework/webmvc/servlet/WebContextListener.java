package org.berlinframework.webmvc.servlet;

import org.berlinframework.context.support.AbstractApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Abhilash Krishnan
 */
@WebListener
public class WebContextListener implements ServletContextListener {
    Logger LOGGER = Logger.getLogger(WebContextListener.class.getName());
    private WebContextLoader loader;
    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("Starting Berlin Framework version 0.1");
        LOGGER.info("To download updates please visit http://www.berlinframework.org");

        this.servletContext = servletContextEvent.getServletContext();

        loader = new WebContextLoader(servletContext);
        loader.load();
        this.servletContext.setAttribute("loader", loader);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Map<String, Object> container = this.loader.getApplicationContext().getBeans();
        container.clear();
        container = null; //GC
        this.loader = null; //GC
        this.servletContext.setAttribute("loader", null);
    }
}
