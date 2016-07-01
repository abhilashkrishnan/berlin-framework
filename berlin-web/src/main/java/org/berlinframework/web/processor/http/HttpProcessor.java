package org.berlinframework.web.processor.http;

import org.berlinframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http request processor using Chain of Responsibility Pattern
 * @author Abhilash Krishnan
 */
public class HttpProcessor {
    protected WebApplicationContext applicationContext;
    protected HttpProcessor nextProcessor;

    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addProcessor(HttpProcessor processor) {
        if(nextProcessor == null)
            this.nextProcessor = processor;
        else this.nextProcessor.addProcessor(processor);
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, Object bean) {
        this.nextProcessor.process(req,resp, bean);
    }
}
