package org.berlinframework.context.annotation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.berlinframework.beans.factory.ApplicationContext;
import org.berlinframework.context.support.AbstractApplicationContext;
import org.berlinframework.stereotype.Component;
import org.berlinframework.stereotype.Controller;
import org.berlinframework.stereotype.Repository;
import org.berlinframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Collection;

/**
 * @author Abhilash Krishnan
 */
public class BeanAnnotationProcessor extends AnnotationProcessor {

    @Override
    public void process() {
        ClassLoader cl = ((AbstractApplicationContext) this.applicationContext).getClassLoader();

        URLClassLoader urlCl = null;

        if (cl instanceof URLClassLoader)
            urlCl = (URLClassLoader) cl;
        else throw new RuntimeException("Unsupported class loader instance");

        for (URL url : urlCl.getURLs()) {
            try {
                String fileUrl = url.getFile();
                fileUrl = URLDecoder.decode(fileUrl, "UTF-8");
                File classDir = new File(fileUrl);
                if (classDir.isDirectory()) {
                    Collection<File> files = FileUtils.listFiles(classDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
                    files.forEach((f) -> {
                        String name = StringUtils.substringAfter(f.getPath(), classDir.getPath() + "\\");
                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.indexOf(".class"));
                            String className = name.replace('\\', '.');
                            try {
                                Class<?> clazz = cl.loadClass(className);

                                if (!clazz.getName().equals(this.applicationContext.getClass().getTypeName()) && !clazz.isAnnotation() && !clazz.isInterface() && !clazz.isEnum() && !(clazz.getModifiers() == Modifier.ABSTRACT)) {
                                    this.processClass(clazz);
                                }
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(nextProcessor != null)
            nextProcessor.process();
    }

    public void processClass(Class<?> clazz){
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            if(annotation instanceof Bean || annotation instanceof Component || annotation instanceof Controller || annotation instanceof Service || annotation instanceof Repository) {
                try {
                    //Create instances of fields declared in a Bean
                    Field[] fields = clazz.getDeclaredFields();
                    for ( Field field : fields ) {
                        field.setAccessible(true);
                        if(AnnotationUtils.isFieldAutoWired(field)) {
                            if(AnnotationUtils.isFieldQualifierApplied(field)) {
                                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                                if(this.applicationContext.contains(qualifier.name()))
                                    throw new RuntimeException("Duplicate bean qualifier");
                                else {
                                    if (!field.getType().isAssignableFrom(ApplicationContext.class))
                                        this.applicationContext.getBeans().put(qualifier.name(), field.getType().newInstance());
                                }
                            }
                            else {
                                if(!this.applicationContext.contains(field.getType().getName()))
                                    if (!field.getType().isAssignableFrom(ApplicationContext.class))
                                        this.applicationContext.getBeans().put(field.getType().getName(), field.getType().newInstance());
                            }
                        }
                    }
                    if (!this.applicationContext.contains(clazz.getName()))
                        this.applicationContext.getBeans().put(clazz.getName(), clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
