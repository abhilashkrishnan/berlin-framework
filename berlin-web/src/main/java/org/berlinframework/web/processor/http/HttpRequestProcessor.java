package org.berlinframework.web.processor.http;

import org.berlinframework.util.ReflectionUtils;
import org.berlinframework.web.annotation.GET;
import org.berlinframework.web.annotation.POST;
import org.berlinframework.web.annotation.Path;
import org.berlinframework.web.annotation.PathParam;
import org.berlinframework.web.bind.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhilash Krishnan
 */
public class HttpRequestProcessor extends HttpProcessor {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, Object bean) {

        Class<?> clazz = bean.getClass();
        //Path from Controller
        Path path = clazz.getAnnotation(Path.class);
        if(path == null)
            throw new RuntimeException("Invalid path declaration");

        String beanPath = path.value();
        String requestUri = req.getRequestURI();
        //We need the path from request uri minus bean path to evaluate the path in methods
        String pathMinusBeanPath = requestUri.substring(req.getContextPath().length() + beanPath.length());
        Method[] methods = clazz.getDeclaredMethods();

        if (pathMinusBeanPath == null || pathMinusBeanPath.equals("") || pathMinusBeanPath.equals("/")) {

            //Request URI format will be / and /beanpath
            /*Handle only default GET with or w/o query params and POST methods*/
            for (Method method : methods) {
                Path methodPath = method.getAnnotation(Path.class);

                if (methodPath == null) {
                    //Default Methods which may handle query parameters from GET requests
                    //Get default method for GET or POST request. No Path annotation will be used for such methods
                    if (req.getMethod().equals("GET")) {
                        if (method.getAnnotation(GET.class) != null) {

                            String queryString = req.getQueryString();
                            if(queryString == null) {
                                if(method.getParameters().length == 0) {
                                    Object[] noParams = new Object[0];
                                    Object result = ReflectionUtils.invoke(method, bean, noParams);
                                    this.processNext(req, resp, result);
                                    return;
                                }
                            }
                            else {
                                Map<String, String> queryParamLookup = new HashMap<>();

                                String[] queryStringTokens = queryString.split("&");

                                if (queryStringTokens != null || queryStringTokens.length > 0) {
                                    for (String nameValuePairToken : queryStringTokens) {
                                        String[] nameValuePair = nameValuePairToken.split("=");
                                        queryParamLookup.put(nameValuePair[0], nameValuePair[1]);
                                    }
                                } else {
                                    //only one name value pair in the query string
                                    String[] nameValuePair = queryString.split("=");
                                    queryParamLookup.put(nameValuePair[0], nameValuePair[1]);
                                }

                                if (!queryParamLookup.isEmpty()) {
                                    Annotation[][] paramAnnotations = method.getParameterAnnotations();

                                    if (queryParamLookup.size() != paramAnnotations.length)
                                        throw new RuntimeException("Invalid parameters");

                                    Object[] params = new Object[paramAnnotations.length];
                                    int i = 0;
                                    for (Annotation[] annotations : paramAnnotations) {
                                        for (Annotation annotation : annotations) {
                                            PathParam pathParam = (PathParam) annotation;
                                            params[i++] = queryParamLookup.get(pathParam.value());
                                        }
                                    }
                                    Object result = ReflectionUtils.invoke(method, bean, params);
                                    //resp.setContentType("application/json");
                                    this.processNext(req, resp, result);
                                    return;
                                }
                            }
                        }
                    } else if (req.getMethod().equals("POST")) {
                        if (method.getAnnotation(POST.class) != null) {

                            int contentLength = req.getContentLength();
                            char[] buffer = new char[contentLength];

                            try {
                                req.getReader().read(buffer, 0, contentLength);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            String content = new String(buffer);
                            Class<?>[] parameters = method.getParameterTypes();

                            if (parameters != null && parameters.length == 1) {
                                String contentType = req.getContentType();
                                if (contentType.equals("application/json")) {
                                    Object o = JsonMapper.jsonToObject(content, parameters[0]);
                                    Object result = ReflectionUtils.invoke(method, bean, o);
                                    //resp.setContentType("application/json");
                                    this.processNext(req, resp, result);
                                }
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            //Request with query params or path params
            for (Method method : methods) {
                if (method != null) {

                    Path methodPath = method.getAnnotation(Path.class);
                    if(methodPath != null) {
                        Annotation[] methodAnnotations = method.getAnnotations();

                        for (Annotation methodAnnotation : methodAnnotations) {
                            if (methodAnnotation instanceof GET) {

                                String methodPathValue = methodPath.value();

                                String[] methodPathValueMatches = methodPathValue.split("/");//RegexUtils.positiveLookAheadMatches(pathValue, "/");

                                String[] pathMinusBeanPathMatches = pathMinusBeanPath.split("/"); //RegexUtils.positiveLookAheadMatches(requestPathMinusPath, "/");

                                //If Query String present
                                String queryString = req.getQueryString();

                                if (queryString != null) {
                                    Map<String, String> queryParamLookup = new HashMap<>();

                                    String[] queryStringTokens = queryString.split("&");

                                    if (queryStringTokens != null || queryStringTokens.length > 0) {
                                        for (String nameValuePairToken : queryStringTokens) {
                                            String[] nameValuePair = nameValuePairToken.split("=");
                                            queryParamLookup.put(nameValuePair[0], nameValuePair[1]);
                                        }
                                    } else {
                                        //only one name value pair in the query string
                                        String[] nameValuePair = queryString.split("=");
                                        queryParamLookup.put(nameValuePair[0], nameValuePair[1]);
                                    }

                                    if (!queryParamLookup.isEmpty()) {
                                        Annotation[][] paramAnnotations = method.getParameterAnnotations();

                                        if(queryParamLookup.size() != paramAnnotations.length)
                                            throw new RuntimeException("Invalid parameters");

                                        Object[] params = new Object[paramAnnotations.length];
                                        int i = 0;
                                        for (Annotation[] annotations : paramAnnotations) {
                                            for (Annotation annotation : annotations) {
                                                PathParam pathParam = (PathParam) annotation;
                                                params[i++] = queryParamLookup.get(pathParam.value());
                                            }
                                        }
                                        Object result = ReflectionUtils.invoke(method, bean, params);
                                        //resp.setContentType("application/json");
                                        this.processNext(req, resp, result);
                                        return;
                                    }
                                } else { //Not a Query String

                                    if (methodPathValue.equals(pathMinusBeanPath)) {
                                        //Invoke the method straight away
                                        //No param or variable
                                        Object[] noParams = new Object[0];
                                        Object result = ReflectionUtils.invoke(method, bean, noParams);
                                        /*
                                            Will need two annotations - @Produces @Consumes
                                            Read @Produces and set content type here
                                         */
                                        //resp.setContentType("application/json");
                                        this.processNext(req, resp, result);
                                        return;
                                    } else {

                                        //For request with path variables
                                        boolean allVariables = false;

                                        for (String p : methodPathValueMatches) {
                                            if (p.matches("\\{(.*?)\\}"))
                                                allVariables = true;
                                            else {
                                                allVariables = false;
                                                break;
                                            }
                                        }

                                        if (allVariables) {
                                            //if path value matches are all variables then match the length of two arrays
                                            if (methodPathValueMatches.length == pathMinusBeanPathMatches.length) {
                                                Map<String, String> paramLookUp = new HashMap<>();
                                                int i = 0;
                                                //Strip braces from the path value
                                                for (String v : methodPathValueMatches)
                                                    paramLookUp.put(v.replaceAll("[^a-zA-Z0-9\\-_]", ""), pathMinusBeanPathMatches[i++]);

                                                Object[] params = new Object[paramLookUp.size()];

                                                int j = 0;
                                                Annotation[][] paramAnnotations = method.getParameterAnnotations();
                                                for (Annotation[] annotations : paramAnnotations) {
                                                    for (Annotation annotation : annotations) {
                                                        PathParam pathParam = (PathParam) annotation;
                                                        params[j++] = paramLookUp.get(pathParam.value());
                                                    }
                                                }
                                                Object result = ReflectionUtils.invoke(method, bean, params);
                                                //resp.setContentType("application/json");
                                                this.processNext(req, resp, result);
                                                return;
                                            }
                                        } else {

                                            //There will be some path variables and text as well E.g. /hello/{msg}/now
                                            Map<String, String> paramLookUp = new HashMap<>();
                                            int i = 0;
                                            boolean pathMatch = false;
                                            for (String v : methodPathValueMatches) {
                                                if (!v.matches("\\{(.*?)\\}")) {
                                                    if (v.equals(pathMinusBeanPathMatches[i]))
                                                        pathMatch = true;
                                                    else pathMatch = false;
                                                }
                                                i++;
                                            }
                                            int j = 0;
                                            if (pathMatch) {
                                                for (String v : methodPathValueMatches) {
                                                    if (v.matches("\\{(.*?)\\}")) {
                                                        //Strip braces from the path value
                                                        paramLookUp.put(v.replaceAll("[^a-zA-Z0-9\\-_]", ""), pathMinusBeanPathMatches[j]);
                                                    }
                                                    j++;
                                                }

                                                Object[] params = new Object[paramLookUp.size()];

                                                int k = 0;
                                                Annotation[][] paramAnnotations = method.getParameterAnnotations();
                                                for (Annotation[] annotations : paramAnnotations) {
                                                    for (Annotation annotation : annotations) {
                                                        PathParam pathParam = (PathParam) annotation;
                                                        params[k++] = paramLookUp.get(pathParam.value());
                                                    }
                                                }
                                                Object result = ReflectionUtils.invoke(method, bean, params);
                                                //resp.setContentType("application/json");
                                                this.processNext(req, resp, result);
                                                return;
                                            }
                                        }

                                    }
                                }//Not Query String
                            }//GET
                            else if (methodAnnotation instanceof POST) {
                                //do Post
                                String methodPathValue = methodPath.value();

                                if(methodPathValue != null) {

                                    if (methodPathValue.equals(pathMinusBeanPath)) {

                                        int contentLength = req.getContentLength();
                                        char[] buffer = new char[contentLength];

                                        try {
                                            req.getReader().read(buffer, 0, contentLength);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }

                                        String content = new String(buffer);
                                        Class<?>[] parameters = method.getParameterTypes();

                                        if (parameters != null && parameters.length == 1) {
                                            String contentType = req.getContentType();
                                            if (contentType.equals("application/json")) {
                                                Object o = JsonMapper.jsonToObject(content, parameters[0]);
                                                Object result = ReflectionUtils.invoke(method, bean, o);
                                                //resp.setContentType("application/json");
                                                this.processNext(req, resp, result);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else throw new RuntimeException("Unable to resolve path");
            }
        }
    }

    protected void processNext(HttpServletRequest req, HttpServletResponse resp, Object bean) {
        if(this.nextProcessor != null)
            this.nextProcessor.process(req, resp, bean);
    }
}
