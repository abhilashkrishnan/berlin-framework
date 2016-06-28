package org.berlinframework.webmvc.servlet.annotation;

import org.berlinframework.stereotype.Controller;
import org.berlinframework.webmvc.servlet.WebApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by Abhilash Krishnan on 28-06-2016.
 */
public class ControllerAnnotationProcessor {
    private WebApplicationContext webApplicationContext;


    public void setBeanFactory(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    public void process(){

        this.webApplicationContext.getBeans().forEach((k,v) -> {
            Object bean = v;
            AnnotatedElement element = bean.getClass();
            Annotation[] annotations = element.getAnnotations();

            for (Annotation annotation : annotations) {
                if(annotation instanceof Controller) {
                    Controller controller = (Controller) annotation;
                    this.webApplicationContext.getControllers().put(controller.path(),bean);
                }
            }
        });

    }
}
