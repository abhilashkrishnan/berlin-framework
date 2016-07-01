package org.berlinframework.context.annotation;

import org.berlinframework.beans.factory.ApplicationContext;
import org.berlinframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Abhilash Krishnan
 */
public class AutoWiredAnnotationProcessor extends AnnotationProcessor{

	public void process() {

		this.applicationContext.getBeans().forEach((k, v) -> {
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
							Object bean = this.applicationContext.getBean(paramClass.getName());
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
					if(field.getType().isAssignableFrom(ApplicationContext.class))
						params[i++] = this.applicationContext.getBean(this.applicationContext.getClass().getName());
					else
						params[i++] = this.applicationContext.getBean(qualifier.name());
				}
				else {
					if (field.getType().isAssignableFrom(ApplicationContext.class))
						params[i++] = this.applicationContext.getBean(this.applicationContext.getClass().getName());
					else
						params[i++] = this.applicationContext.getBean(field.getType().getName());
				}
			}
		}

		if(i > 0) {
				Object bean = this.applicationContext.getBean(clazz.getName());

				if(bean != null)
					ReflectionUtils.invoke(method, bean, params);
		}
	}
}
