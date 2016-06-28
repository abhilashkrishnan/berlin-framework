package org.brelin.beans.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import org.brelin.beans.factory.BeanFactory;

public class AutoWiredAnnotationProcessor {
	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void process() {
		this.beanFactory.getBeans().forEach((k, v) -> {
			Class<?> beanClass = v.getClass();
			Method[] methods = beanClass.getDeclaredMethods();
			for (Method method : methods) {
				Annotation[] annotations = method.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof AutoWired) {
						Class<?>[] paramTypes = method.getParameterTypes();
						Object[] params = new Object[paramTypes.length];

						int i = 0;
						for (Class<?> paramClass : paramTypes) {
							Set<String> names = this.beanFactory.getBeans().keySet();
							for (String name : names) {
								Object bean = this.beanFactory.getBeans().get(name);
								
								if (bean.getClass().getName().equals(paramClass.getName())) {
									params[i++] = bean;
								}
							}
						}
						method.setAccessible(true);
						try {
							method.invoke(v, params);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		});
	}
}
