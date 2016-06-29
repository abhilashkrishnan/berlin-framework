package org.berlinframework.context.annotation;

import org.berlinframework.stereotype.Controller;

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

						method.setAccessible(true);
						try {
							method.invoke(v, params);
						} catch (IllegalAccessException | InvocationTargetException e) {
							throw new RuntimeException(e);
						}
						wired = true;
					}
				}
				// If not wired check for AutoWired fields and inject to method
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
					params[i++] = this.beanFactory.getBean(qualifier.name());
				}
				else params[i++] = this.beanFactory.getBean(field.getType().getName());
			}
		}

		if(i > 0) {
			method.setAccessible(true);
			try {
				Object bean = null;
				Controller controller = clazz.getAnnotation(Controller.class);

				if(controller != null)
					bean = this.beanFactory.getBean(controller.path());
				else bean = this.beanFactory.getBean(clazz.getName());

				if(bean != null)
					method.invoke(bean, params);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
