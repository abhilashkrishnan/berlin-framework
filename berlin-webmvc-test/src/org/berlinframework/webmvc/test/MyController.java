package org.berlinframework.webmvc.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.berlinframework.context.annotation.AutoWired;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.webmvc.servlet.WebApplicationContext;


@Controller(path="/my")
public class MyController {
	
	/*
	 * In a type Berlin WebMVC controller it is advised to auto wire WebApplicationContext instead of ApplicationContext 
	 */
	@AutoWired
	private WebApplicationContext applicationContext;
	
	public void setApplicationContext(WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/*
	 * Dummy implementation to handle http request GET request.
	 * Will change it to a fully blown implementation later
	 */
	public void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MyFirstBean mfb = (MyFirstBean) applicationContext.getBean("first");
		mfb.setName("Berlin Framework");
		SecondBean sb = (SecondBean) applicationContext.getBean(SecondBean.class.getName());
		resp.getWriter().write("<p>Hello from "+ sb.getMyFirstBean().getName()+ "<p>");
		resp.getWriter().write("<p>" + sb.getThirdBean().getMessage() + "<p>");
		resp.getWriter().flush();
		
	}
}
