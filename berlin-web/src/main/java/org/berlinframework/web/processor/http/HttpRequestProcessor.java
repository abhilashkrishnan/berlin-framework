package org.berlinframework.web.processor.http;

import org.berlinframework.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Abhilash Krishnan
 */
public class HttpRequestProcessor {
    protected BeanFactory beanFactory;
    protected HttpRequestProcessor nextProcessor;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void addProcessor(HttpRequestProcessor processor) {
        if(nextProcessor == null)
            this.nextProcessor = processor;
        else this.nextProcessor.addProcessor(processor);
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, Object bean) {
        this.nextProcessor.process(req,resp, bean);
    }
}
