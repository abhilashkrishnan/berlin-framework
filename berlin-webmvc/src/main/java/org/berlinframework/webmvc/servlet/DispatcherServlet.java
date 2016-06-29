package org.berlinframework.webmvc.servlet;

import org.berlinframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Abhilash Krishnan
 */
@WebServlet(name="dispatcher",urlPatterns="/*", loadOnStartup=2)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WebContextLoader loader;

    public DispatcherServlet() {

    }

    @Override
    public void init() throws ServletException {
        this.loader = (WebContextLoader) getServletContext().getAttribute("loader");
    }

    /*
	 * Currently it is a very simple implementation. Will make it better
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.route(req, resp);
    }

    /*
	 * Currently it is a very simple implementation. Will make it better
	 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.route(req, resp);
    }

    private void route(HttpServletRequest req, HttpServletResponse resp) {
        WebApplicationContext webApplicationContext = (WebApplicationContext) this.loader.getBeanFactory();
        Object controller = webApplicationContext.getBean(req.getRequestURI().substring(req.getContextPath().length()));
        if(controller != null) {
            Class<?> clazz = controller.getClass();
            try {
            /* Will remove hardcoded method names */
                Method method = null;

                if(req.getMethod().equals("GET"))
                    method = clazz.getDeclaredMethod("get", HttpServletRequest.class, HttpServletResponse.class);
                else if(req.getMethod().equals( "POST")) method = clazz.getDeclaredMethod("post", HttpServletRequest.class, HttpServletResponse.class);

                if (method != null)
                    method.invoke(controller, req, resp);
                else new RuntimeException("Undefined operation");

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
}
