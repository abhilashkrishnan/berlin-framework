package org.berlinframework.beans.factory;

import org.berlinframework.util.RegexUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanFactory {
	private Map<String, Object> beans = new HashMap<>();
	private ClassLoader classLoader;

	public Object getBean(String name) {
		if (name.contains("/")) {
			List<String> keyOne = this.beans.keySet().stream().filter( k -> RegexUtils.match(name, ("^"+k).replaceAll("/", "\\/"))).collect(Collectors.toList());
			if(!keyOne.isEmpty())
				return this.beans.get(keyOne.get(0));
			else return null;
		}
		else return this.beans.get(name);
	}

	public Object getBean(Class<?> clazz) {
		Object bean = this.beans.entrySet().stream().filter( m -> m.getKey().equals(clazz.getName()) && m.getValue().getClass().getTypeName().equals(clazz.getName()))
				.map(m -> m.getValue());
		return bean;
	}

	public Map<String, Object> getBeans() {
		return this.beans;
	}

	public boolean contains(String name) {
		return this.beans.containsKey(name);
	}

	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
}