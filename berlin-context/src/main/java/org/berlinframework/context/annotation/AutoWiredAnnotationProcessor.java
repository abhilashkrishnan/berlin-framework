package org.berlinframework.context.annotation;

import org.berlinframework.stereotype.Controller;
import org.berlinframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * @author Abhilash Krishnan
 */
public class AutoWiredAnnotationProcessor extends AnnotationProcessor{

	public void process() {

		this.beanFactory.getBeans().forEach((k, v) -> {
			Class<?> beanClass = v.getClass();

			Method[] methods = beanClass.getDeclaredMethods();
			for (Method method : methods) {
				boolean wired = false;

				// Check for AutoWired methods
				Annotation[] annotations = method.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof AutoWired) {
						Class<?>[] paramTypes = method.getParameterTypes();
						Object[] params = new Object[paramTypes.length];

						int i = 0;
						for (Class<?> paramClass : paramTypes) {
							Object bean = this.beanFactory.getBean(paramClass.getName());
							params[i++] = bean;
						}

						ReflectionUtils.invoke(method, v, params);
						wired = true;
					}
				}
				// If method is not AutoWired check for AutoWired fields in the bean class and inject to method
				if(!wired)
					autoWireFieldsToMethod(beanClass, method);
			}
		});

		if(nextProcessor != null)
			nextProcessor.process();
	}

	private void autoWireFieldsToMethod(Class<?> clazz, Method method) {
		Parameter[] parameters = method.getParameters();
		Object[] params = new Object[parameters.length];

		int i = 0;

		for ( Parameter parameter : parameters ) {
			Field field = AnnotationUtils.getAutoWiredField(clazz, parameter.getType());
			if (field != null) {
				if( AnnotationUtils.isFieldQualifierApplied(field)) {
					Qualifier qualifier = field.getAnnotation(Qualifier.class);
					if(field.getType().getName().equals("org.berlinframework.beans.factory.BeanFactory")
							|| field.getType().getName().equals("org.berlinframework.beans.factory.ApplicationContext")
							|| field.getType().getName().equals("org.berlinframework.webmvc.servlet.WebApplicationContext"))
						params[i++] = this.beanFactory.getBean(this.beanFactory.getClass().getName());
					else
						params[i++] = this.beanFactory.getBean(qualifier.name());
				}
				else {
					if(field.getType().getName().equals("org.berlinframework.beans.factory.BeanFactory")
							|| field.getType().getName().equals("org.berlinframework.beans.factory.ApplicationContext")
							|| field.getType().getName().equals("org.berlinframework.webmvc.servlet.WebApplicationContext"))
						params[i++] = this.beanFactory.getBean(this.beanFactory.getClass().getName());
					else
						params[i++] = this.beanFactory.getBean(field.getType().getName());
				}
			}
		}

		if(i > 0) {
				Object bean = this.beanFactory.getBean(clazz.getName());

				if(bean != null)
					ReflectionUtils.invoke(method, bean, params);
		}
	}
}
