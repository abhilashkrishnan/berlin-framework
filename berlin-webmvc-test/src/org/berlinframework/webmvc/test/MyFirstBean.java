package org.berlinframework.webmvc.test;

import org.berlinframework.context.annotation.Bean;

/**
 * @author Abhilash Krishnan
 */
@Bean
public class MyFirstBean {
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
