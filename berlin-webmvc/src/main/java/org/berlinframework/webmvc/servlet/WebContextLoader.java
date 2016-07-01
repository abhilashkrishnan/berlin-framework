package org.berlinframework.webmvc.servlet;

import org.berlinframework.context.annotation.AnnotationProcessor;
import org.berlinframework.context.annotation.AutoWiredAnnotationProcessor;
import org.berlinframework.context.annotation.BeanAnnotationProcessor;
import org.berlinframework.web.context.WebApplicationContext;
import org.berlinframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author Abhilash Krishnan
 */
public class WebContextLoader {
    private WebApplicationContext applicationContext;
    private ServletContext servletContext;

    public WebContextLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.applicationContext = new GenericWebApplicationContext(this.servletContext);
    }

    public void load() {
        this.applicationContext.getBeans().put(this.applicationContext.getClass().getName(), applicationContext);
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        annotationProcessor.setApplicationContext(this.applicationContext);
        annotationProcessor.addProcessor(new BeanAnnotationProcessor());
        annotationProcessor.addProcessor(new AutoWiredAnnotationProcessor());
        annotationProcessor.process();
    }

    public WebApplicationContext getApplicationContext() {
        return this.applicationContext;
    }
}
