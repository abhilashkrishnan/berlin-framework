package org.berlinframework.beans.factory;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
	private Map<String, Object> beans = new HashMap<>();
	private ClassLoader classLoader;
	
	public Object getBean(String name) {
		return this.beans.get(name);
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