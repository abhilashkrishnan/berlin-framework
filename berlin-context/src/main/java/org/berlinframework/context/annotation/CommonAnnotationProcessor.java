package org.berlinframework.context.annotation;

import org.berlinframework.beans.factory.BeanFactory;
import org.berlinframework.stereotype.Component;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.stereotype.Repository;
import org.berlinframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

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
                    //Create instances of fields declared in a Bean
                    Field[] fields = clazz.getDeclaredFields();
                    for ( Field field : fields ) {
                        field.setAccessible(true);
                        if(AnnotationUtils.isFieldAutoWired(field)) {
                            if(AnnotationUtils.isFieldQualifierApplied(field)) {
                                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                                if(this.beanFactory.contains(qualifier.name()))
                                    throw new RuntimeException("Duplicate bean name");
                                else this.beanFactory.getBeans().put(qualifier.name(), field.getType().newInstance());
                            }
                            else {
                                if(!this.beanFactory.contains(field.getType().getName()))
                                    this.beanFactory.getBeans().put(field.getType().getName(), field.getType().newInstance());
                            }
                        }
                    }
                    //Create instance of Bean
                    if(!this.beanFactory.contains(clazz.getName()))
                        this.beanFactory.getBeans().put(clazz.getName(),clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
