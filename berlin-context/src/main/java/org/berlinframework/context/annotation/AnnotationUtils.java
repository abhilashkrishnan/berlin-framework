package org.berlinframework.context.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Abhilash Krishnan
 */
public class AnnotationUtils {

    public static Field getAutoWiredField(Class<?> clazz, Class<?> type) {
        Field[] fields = clazz.getDeclaredFields();
        for ( Field field : fields) {
            field.setAccessible(true);
            if(isFieldAutoWired(field) && field.getType().getName().equals(type.getName()))
                return field;
        }
        return null;
    }
    public static boolean isFieldAutoWired(Field field) {
        return field.getAnnotation(AutoWired.class) != null ? true : false;
    }
}