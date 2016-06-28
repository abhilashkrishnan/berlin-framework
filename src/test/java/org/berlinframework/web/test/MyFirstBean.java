package org.berlinframework.web.test;

import org.berlinframework.beans.annotation.Bean;

/**
 * Created by ACER on 27-06-2016.
 */
@Bean(name="myFirstBean")
public class MyFirstBean {
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
