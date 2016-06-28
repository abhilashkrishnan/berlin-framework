package org.brelin.beans.factory;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
	private Map<String, Object> beans = new HashMap<>();
	
	public Object getBean(String name) {
		return this.beans.get(name);
	}
	
	public Map<String, Object> getBeans() {
		return this.beans;
	}
}