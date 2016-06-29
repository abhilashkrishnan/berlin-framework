package org.berlinframework.context.annotation;

import org.berlinframework.beans.factory.BeanFactory;

/**
 * Chain of Annotation Processors implementation following Chain of Responsibility Design Pattern
 * @author Abhilash Krishnan
 */
public class AnnotationProcessor {
    protected AnnotationProcessor nextProcessor;
    protected BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void addProcessor(AnnotationProcessor processor) {
        if (nextProcessor == null)
            nextProcessor = processor;
        else nextProcessor.addProcessor(processor);
        nextProcessor.setBeanFactory(this.beanFactory);
    }

    public void process() {
        if(nextProcessor != null)
            nextProcessor.process();
    }
}
