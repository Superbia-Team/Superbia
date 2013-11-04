package com.dm.estore.config;

import com.dm.estore.server.servlet.ApplicationFilter;
import com.dm.estore.server.servlet.StaticContentServlet;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 8:43 PM
 */
public class WebAppInitializer implements WebApplicationInitializer {

    private static final String STATIC_SERVLET_NAME = "static-resources";
    private static final String STATIC_SERVLET_MAPPING = "/*";
    private static final String APP_FILTER_MAPPING = "*";
    private static final String APP_FILTER_NAME = "application-filter";
    private static final String REST_SERVLET_NAME = "jersey-serlvet";
    private static final String REST_SERVLET_MAPPING = "/api/*";

    private static final String REST_CONTROLLERS_PARAMETER = "com.sun.jersey.config.property.packages";
    private static final String REST_CONTROLLERS_PACKAGE = "com.dm.estore.controllers";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        registerListener(servletContext);
        registerServlets(servletContext);
        registerFilters(servletContext);
    }

    private void registerListener(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = createContext(ApplicationModule.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }

    private void registerServlets(ServletContext servletContext) {

        // Static web resources dispatcher
        ServletRegistration.Dynamic staticResourcesDispatcher = servletContext.addServlet(STATIC_SERVLET_NAME, new StaticContentServlet());
        staticResourcesDispatcher.setLoadOnStartup(1);
        staticResourcesDispatcher.addMapping(STATIC_SERVLET_MAPPING);

        // REST API dispatcher
        ServletRegistration.Dynamic restDispatcher = servletContext.addServlet(REST_SERVLET_NAME, new SpringServlet());
        restDispatcher.setInitParameter(REST_CONTROLLERS_PARAMETER, REST_CONTROLLERS_PACKAGE);
        restDispatcher.setLoadOnStartup(1);
        restDispatcher.addMapping(REST_SERVLET_MAPPING);
    }

    private void registerFilters(ServletContext servletContext) {
        FilterRegistration.Dynamic applicationFilter = servletContext.addFilter(APP_FILTER_NAME, new ApplicationFilter());
        applicationFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, APP_FILTER_MAPPING);
    }

    private AnnotationConfigWebApplicationContext createContext(final Class<?>... modules) {
        AnnotationConfigWebApplicationContext _ctx = new AnnotationConfigWebApplicationContext();
        _ctx.register(modules);
        return _ctx;
    }
}
