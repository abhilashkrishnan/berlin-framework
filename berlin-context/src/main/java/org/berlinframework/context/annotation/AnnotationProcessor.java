package org.berlinframework.context.annotation;

import org.berlinframework.beans.factory.ApplicationContext;

/**
 * Chain of Annotation Processors implementation following Chain of Responsibility Design Pattern
 * @author Abhilash Krishnan
 */
public class AnnotationProcessor {
    protected AnnotationProcessor nextProcessor;
    protected ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addProcessor(AnnotationProcessor processor) {
        if (nextProcessor == null)
            nextProcessor = processor;
        else nextProcessor.addProcessor(processor);
        nextProcessor.setApplicationContext(this.applicationContext);
    }

    public void process() {
        if(nextProcessor != null)
            nextProcessor.process();
    }
}
