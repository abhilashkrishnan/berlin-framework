package org.berlinframework.webmvc.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.berlinframework.beans.factory.ApplicationContext;
import org.berlinframework.context.annotation.AutoWired;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.web.annotation.GET;
import org.berlinframework.web.annotation.Path;
import org.berlinframework.web.annotation.PathParam;
import org.berlinframework.webmvc.servlet.WebApplicationContext;

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
		System.out.println("Hello GET method");
		MyFirstBean mfb = (MyFirstBean) applicationContext.getBean("first");
		mfb.setName("Berlin Framework");
		
		SecondBean sb = (SecondBean) applicationContext.getBean(SecondBean.class.getName());
		System.out.println(sb.getMyFirstBean().getName());
		System.out.println(sb.getThirdBean().getMessage());
		return sb;
	}
	
	/*
	 * Example to demonstrate parameter usage   
	 */
	@Path("/echo/{message}")
	@GET
	public String echo(@PathParam("message")String msg) {
		System.out.println("Hello echo() GET method");
		System.out.println("Message from request is "+msg);
		return msg;
	}
	
	@GET
	public void defHello() {
		System.out.println("Default GET method");
	}
	
	/*
	 * Example to demonstrate usage of Query Params
	 * Example request - /my?name=Abhilash&city=Bangalore
	 */
	@GET
	public String param(@PathParam("name")String name, @PathParam("city")String city) {
		System.out.println("Query Params are name="+ name + " and city="+city);
		return "Query Params are name="+name+" and city="+city;
	}
	
	/*
	 * More Query Params
	 * Example request - /my/info?name=Abhilash&city=Bangalore
	 */
	@Path("/info")
	@GET
	public String info(@PathParam("name")String name, @PathParam("city")String city) {
		System.out.println("Query Params are name="+ name + " and city="+city);
		return "Query Params are name="+name+" and city="+city;
	}
}
