package org.berlinframework.web.processor.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Abhilash Krishnan
 */
public class HttpMethodProcessor extends RequestProcessor {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, Object bean) {
        super.process(req, resp, bean);
    }
}
