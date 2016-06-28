package org.berlinframework.web;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.berlinframework.web.annotation.Controller;

/**
 * Web application context loader servlet
 */
@WebServlet(name="contextLoader",urlPatterns="/", loadOnStartup=1)
public class WebContextLoaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WebContextLoader loader;
	
    /**
     * Default constructor. 
     */
    public WebContextLoaderServlet() {
    }
    
    
    @Override
    public void init() throws ServletException {
    	System.out.println("Starting Berlin Framework");
    	System.out.println("Features: ");
    	System.out.println("Dependency Injection");
    	System.out.println("Front Controller");
    	System.out.println("MVC Controller");
    	loader = new WebContextLoader(getServletContext());
		loader.load();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	//Load controllers - Very Simple Implementation
    	loader.getBeanFactory().getBeans().forEach((k,v) -> {
    	Object obj = v;
    	Class<?> clazz = obj.getClass();
    	Annotation[] annotations = clazz.getAnnotations();
    	for(Annotation annotation : annotations) {
    		if(annotation instanceof Controller) {
    			try {
					Method method = clazz.getDeclaredMethod("processGet", HttpServletRequest.class, HttpServletResponse.class);
					method.invoke(obj, req, resp);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
    		}
    	}
    	});
    }
}
