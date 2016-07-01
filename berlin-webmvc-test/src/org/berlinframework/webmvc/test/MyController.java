package org.berlinframework.webmvc.test;

import org.berlinframework.beans.factory.ApplicationContext;
import org.berlinframework.context.annotation.AutoWired;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.web.annotation.GET;
import org.berlinframework.web.annotation.Path;
import org.berlinframework.web.annotation.PathParam;

/**
 * Berlin WebMVC Controller implementation
 * @author Abhilash Krishnan
 */

@Controller
@Path("/my")
public class MyController {
	
	/*
	 * Autowire ApplicationContext instance 
	 */
	@AutoWired
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	/*
	 * First and Third Bean is injected and auto wired to Second Bean.   
	 */
	@Path("/hello")
	@GET
	public SecondBean hello() {
		MyFirstBean firstBean = (MyFirstBean) applicationContext.getBean("firstBean");
		firstBean.setName("Berlin Framework");
		
		SecondBean secondBean = (SecondBean) applicationContext.getBean(SecondBean.class.getName());
		return secondBean;
	}
	
	/*
	 * Example to demonstrate parameter usage   
	 */
	@Path("/echo/{message}")
	@GET
	public String echo(@PathParam("message")String message) {
		return message;
	}
	
	@GET
	public String defaultHello() {
		return "Hello from default GET method"; 
	}
	
	/*
	 * Example to demonstrate usage of Query Params
	 * Example request - /my?name=Abhilash&city=Bangalore
	 */
	@GET
	public String param(@PathParam("name")String name, @PathParam("city")String city) {
		return "Query Params are name="+ name +" and city="+ city;
	}
	
	/*
	 * More Query Params
	 * Example request - /my/info?name=Abhilash&city=Bangalore
	 */
	@Path("/info")
	@GET
	public String info(@PathParam("name")String name, @PathParam("city")String city) {
		return "Query Params are name="+ name +" and city="+ city;
	}
}
