package org.berlinframework.context.annotation;

import org.berlinframework.beans.factory.BeanFactory;
import org.berlinframework.stereotype.Component;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.stereotype.Repository;
import org.berlinframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by Abhilash Krishnan on 28-06-2016.
 */
public class CommonAnnotationProcessor {
    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void process(Class<?> clazz, AnnotatedElement element){

        Annotation[] annotations = element.getAnnotations();

        for (Annotation annotation : annotations) {
            if(annotation instanceof Bean || annotation instanceof Component || annotation instanceof Controller || annotation instanceof Service || annotation instanceof Repository) {
                try {
                    this.beanFactory.getBeans().put(clazz.getName(),clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
}
