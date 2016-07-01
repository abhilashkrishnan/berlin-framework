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
public class AutoWiredAnnotationProcessor extends AnnotationProcessor {

	public void process() {

		this.applicationContext.getBeans().forEach((k, v) -> {
			//Autowire fields
			this.autoWireFields(v);

			//Autowire methods
			this.autoWireMethods(v);

			if (nextProcessor != null)
				nextProcessor.process();
		});
	}

	private void autoWireFields(Object bean) {

		Field[] fields = bean.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(AutoWired.class) != null) {
				field.setAccessible(true);

				Object value = null;

				if (field.getType().isAssignableFrom(ApplicationContext.class))
					value = this.applicationContext.getBean(this.applicationContext.getClass().getTypeName());
				else value = this.applicationContext.getBean(field.getType().getName());

				if (value != null)
					try {
						field.set(bean, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
			}
		}
	}

	private void autoWireMethods(Object bean) {
		Class<?> beanClass = bean.getClass();

		Method[] methods = beanClass.getDeclaredMethods();
		for (Method method : methods) {
			boolean wired = false;

			// Check for AutoWired methods
			Annotation annotation = method.getAnnotation(AutoWired.class);

			if (annotation != null) {

				Class<?>[] paramTypes = method.getParameterTypes();
				Object[] params = new Object[paramTypes.length];

				int i = 0;
				for (Class<?> paramClass : paramTypes) {
					Object param = this.applicationContext.getBean(paramClass.getName());
					params[i++] = param;
				}

				ReflectionUtils.invoke(method, bean, params);
				wired = true;
			}

			// If method is not AutoWired yet (i.e. No AutoWired annotation present) check for possible AutoWired fields in the bean class and inject to method
			if(!wired)
				autoWireFieldsToMethod(bean, method);
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
					//Can Qualifier be applied to ApplicationContext??
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
