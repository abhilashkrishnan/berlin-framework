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
	 * To use controllers we should auto wire WebApplicationContext instead of ApplicationContext 
	 */
	private WebApplicationContext applicationContext;
	
	@AutoWired
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
		resp.getWriter().write("Hello from "+sb.getMyFirstBean().getName());
		resp.getWriter().write(sb.getThirdBean().getMessage());
		resp.getWriter().flush();
		
	}
}
