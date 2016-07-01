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

			//Autowire fields
			this.autoWireFields(v);

			//Autowire methods

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
					autoWireFieldsToMethod(v, method);
			}
		});

		if(nextProcessor != null)
			nextProcessor.process();
	}

	private void autoWireFields(Object bean) {

		Field[] fields = bean.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(AutoWired.class) != null ) {
				field.setAccessible(true);

				Object value = null;

				if(field.getType().isAssignableFrom(ApplicationContext.class))
					value = this.applicationContext.getBean(this.applicationContext.getClass().getTypeName());
				else value = this.applicationContext.getBean(field.getType().getName());

				if(value != null)
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
			}
		}
	}

	private void autoWireFieldsToMethod(Object bean, Method method) {
		Class<?> clazz = bean.getClass();
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
			ReflectionUtils.invoke(method, bean, params);
		}
	}
}
