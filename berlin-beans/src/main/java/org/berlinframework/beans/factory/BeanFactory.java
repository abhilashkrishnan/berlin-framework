package org.berlinframework.beans.factory;

import java.util.Map;

public interface BeanFactory {

	Object getBean(String name);

	Object getBean(Class<?> clazz);

	Map<String, Object> getBeans();

	boolean contains(String name);

}