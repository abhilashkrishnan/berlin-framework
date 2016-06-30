package org.berlinframework.webmvc.servlet;

import javax.servlet.ServletContext;

import org.berlinframework.beans.factory.BeanFactory;
import org.berlinframework.context.annotation.AnnotationProcessor;
import org.berlinframework.context.annotation.AutoWiredAnnotationProcessor;
import org.berlinframework.context.annotation.BeanAnnotationProcessor;

/**
 * @author Abhilash Krishnan
 */
public class WebContextLoader {
    private BeanFactory beanFactory;
    private ServletContext servletContext;

    public WebContextLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.beanFactory = new WebApplicationContext(servletContext);
    }

    public void load() {
        this.beanFactory.getBeans().put(this.beanFactory.getClass().getName(), beanFactory);
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        annotationProcessor.setBeanFactory(this.beanFactory);
        annotationProcessor.addProcessor(new BeanAnnotationProcessor());
        annotationProcessor.addProcessor(new AutoWiredAnnotationProcessor());
        annotationProcessor.process();
    }

    public BeanFactory getBeanFactory() {
        return this.beanFactory;
    }
}
