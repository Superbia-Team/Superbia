package com.dm.estore.config;

import com.dm.estore.server.servlet.StaticContentServlet;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 8:43 PM
 */
public class WebAppInitializer implements WebApplicationInitializer {

    private static final String STATIC_SERVLET_NAME = "static-resources";
    private static final String STATIC_SERVLET_MAPPING = "/*";
    private static final String REST_SERVLET_NAME = "jersey-serlvet";
    private static final String REST_SERVLET_MAPPING = "/api/*";

    private static final String REST_CONTROLLERS_PARAMETER = "com.sun.jersey.config.property.packages";
    private static final String REST_CONTROLLERS_PACKAGE = "com.dm.estore.controllers";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        registerListener(servletContext);
        registerServlets(servletContext);
    }

    private void registerListener(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = createContext(ApplicationModule.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }

    private void registerServlets(ServletContext servletContext) {
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(STATIC_SERVLET_NAME, new StaticContentServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(STATIC_SERVLET_MAPPING);

        ServletRegistration.Dynamic restDispatcher = servletContext.addServlet(REST_SERVLET_NAME, new SpringServlet());
        restDispatcher.setInitParameter(REST_CONTROLLERS_PARAMETER, REST_CONTROLLERS_PACKAGE);

        restDispatcher.setLoadOnStartup(1);
        restDispatcher.addMapping(REST_SERVLET_MAPPING);
    }

    private AnnotationConfigWebApplicationContext createContext(final Class<?>... modules) {
        AnnotationConfigWebApplicationContext _ctx = new AnnotationConfigWebApplicationContext();
        _ctx.register(modules);
        return _ctx;
    }
}
