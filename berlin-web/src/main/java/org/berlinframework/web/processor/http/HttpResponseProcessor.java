package org.berlinframework.web.processor.http;

import org.berlinframework.web.bind.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Abhilash Krishnan
 */
public class HttpResponseProcessor extends HttpProcessor {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, Object bean) {
        if(bean != null) {
            try {
                PrintWriter writer = resp.getWriter();
                if(bean instanceof String)
                    writer.write((String) bean);
                else {
                    /* Will cater to Json Xml and other content types */
                    //Current only Json response supported
                    String json = JsonMapper.objectToJson(bean);
                    writer.write(json);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
