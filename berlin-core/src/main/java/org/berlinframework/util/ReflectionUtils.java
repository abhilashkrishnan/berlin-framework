package org.berlinframework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Abhilash Krishnan
 */
public class ReflectionUtils {
    public static Object invoke(Method method, Object bean, Object... params)
    {
        Object result = null;
        try {
            result = method.invoke(bean, params);
        } catch (IllegalAccessException e) {
            new RuntimeException(e);
        } catch (InvocationTargetException e) {
            new RuntimeException(e);
        }
        return result;
    }
}
