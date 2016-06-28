package org.berlinframework.webmvc.test;

import org.berlinframework.context.annotation.Bean;

@Bean
public class ThirdBean {
	private String message = "I am third bean. I am injected through AutoWired method";
	
	
	public String getMessage() {
		return this.message;
	}
}
