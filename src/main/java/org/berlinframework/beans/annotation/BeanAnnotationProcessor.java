package org.berlinframework.beans.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.berlinframework.beans.factory.BeanFactory;
import org.berlinframework.web.annotation.Controller;

public class BeanAnnotationProcessor {
	private BeanFactory beanFactory;
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public void process(Class<?> clazz, AnnotatedElement element){
		
		Annotation[] annotations = element.getAnnotations();

        for (Annotation annotation : annotations) {
        	if(annotation instanceof Bean || annotation instanceof Controller) {
                try {
					this.beanFactory.getBeans().put(clazz.getName(),clazz.newInstance());
				} catch (InstantiationException | IllegalAccessException e1) {
					throw new RuntimeException(e1);
				}
            }
        }
     }

}
