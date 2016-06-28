package org.brelin.web.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brelin.beans.annotation.AutoWired;
import org.brelin.beans.factory.ApplicationContext;
import org.brelin.web.annotation.Controller;

@Controller
public class MyController {

	private ApplicationContext applicationContext;
	
	@AutoWired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/*
	 * Dummy implementation to handle http request GET request.
	 * Will change it to a fully blown implementation later
	 */
	public void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		MyFirstBean mfb = (MyFirstBean) applicationContext.getBean(MyFirstBean.class.getName());
		mfb.setName("Brelin Framework");
		SecondBean sb = (SecondBean) applicationContext.getBean(SecondBean.class.getName());
		resp.getWriter().write("Hello from "+sb.getMyFirstBean().getName());
	}
}
