package org.berlinframework.webmvc.test;

import org.berlinframework.stereotype.Controller;
import org.berlinframework.web.annotation.GET;
import org.berlinframework.web.annotation.Path;

/**
 * 
 * @author Abhilash Krishnan
 * Berlin WebMVC Controller implementation
 * Root or Index Controller available at '/'
 */
@Controller
@Path("/")
public class RootController {

	@GET
	public String index() {
		return "<p>Welcome to my web root!</p> <p>Powered by Berlin Framework</p>";
	}
}
