package org.berlinframework.webmvc.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.berlinframework.beans.factory.BeanFactory;
import org.berlinframework.context.annotation.AutoWiredAnnotationProcessor;
import org.berlinframework.context.annotation.CommonAnnotationProcessor;

public class WebContextLoader {
    private WebApplicationContext webApplicationContext;
    private ServletContext servletContext;

    public WebContextLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.webApplicationContext = new WebApplicationContext(servletContext);
    }

    public void load() {
        this.webApplicationContext.getBeans().put(this.webApplicationContext.getClass().getName(), webApplicationContext);
        this.loadBeans();
        this.wireBeans();
    }

    public BeanFactory getBeanFactory() {
        return this.webApplicationContext;
    }

    private void loadBeans() {
        ClassLoader cl = servletContext.getClassLoader();
        URLClassLoader urlCl = null;

        if (cl instanceof URLClassLoader)
            urlCl = (URLClassLoader) cl;
        else throw new RuntimeException();

        CommonAnnotationProcessor commonAnnotationProcessor = new CommonAnnotationProcessor();
        commonAnnotationProcessor.setBeanFactory(webApplicationContext);

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
                                if (!clazz.getName().equals(this.webApplicationContext.getClass().getName()) && !clazz.isAnnotation() && !clazz.isInterface() && !clazz.isEnum() && !(clazz.getModifiers() == Modifier.ABSTRACT)) {
                                    commonAnnotationProcessor.process(clazz, clazz);
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
    }

    private void wireBeans() {
        AutoWiredAnnotationProcessor autoWiredAnnotationProcessor = new AutoWiredAnnotationProcessor();
        autoWiredAnnotationProcessor.setBeanFactory(webApplicationContext);
        autoWiredAnnotationProcessor.process();
    }

}
