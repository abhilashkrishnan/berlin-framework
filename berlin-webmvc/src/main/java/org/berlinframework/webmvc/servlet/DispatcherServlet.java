package org.berlinframework.webmvc.servlet;

import org.berlinframework.util.RegexUtils;
import org.berlinframework.web.annotation.Path;
import org.berlinframework.web.context.WebApplicationContext;
import org.berlinframework.web.processor.http.HttpProcessor;
import org.berlinframework.web.processor.http.HttpRequestProcessor;
import org.berlinframework.web.processor.http.HttpResponseProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Abhilash Krishnan
 * The Front-Controller servlet
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.route(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new UnsupportedOperationException();
        //this.route(req, resp);
    }

    private void route(HttpServletRequest req, HttpServletResponse resp) {
        WebApplicationContext applicationContext = this.loader.getApplicationContext();
        String requestPathMatcher =  req.getRequestURI().substring(req.getContextPath().length());

        List<Object> beanOne = applicationContext.getBeans().values().stream().filter(v -> {
                Path path = v.getClass().getAnnotation(Path.class);
                if(path != null) {
                    if (requestPathMatcher.equals("/")) return path.value().equals(requestPathMatcher);
                    return RegexUtils.match(requestPathMatcher, ("^" + path.value()).replaceAll("/", "\\/"));
                }
                return false;
        }).collect(Collectors.toList());

        if(!beanOne.isEmpty()) {
            Object bean = beanOne.get(0);
            if (bean == null)
                throw new RuntimeException("No bean available to process the request");
            else {
                HttpProcessor httpProcessor = new HttpProcessor();
                httpProcessor.setApplicationContext(applicationContext);
                httpProcessor.addProcessor(new HttpRequestProcessor());
                httpProcessor.addProcessor(new HttpResponseProcessor());
                httpProcessor.process(req, resp, bean);
            }
        }
    }
}
