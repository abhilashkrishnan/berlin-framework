package org.berlinframework.webmvc.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;



/**
 * @author Abhilash Krishnan
 * Web application context loader servlet
 */
@WebServlet(name="contextLoader",urlPatterns="/berlin-context", loadOnStartup=1)
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
    	System.out.println("Starting Berlin Web MVC Framework");
    	System.out.println("Features: ");
    	System.out.println("Dependency Injection");
    	System.out.println("MVC Controller");
    	loader = new WebContextLoader(getServletContext());
		loader.load();
		getServletContext().setAttribute("loader", loader);
    }

}
