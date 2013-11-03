package com.dm.estore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 9:14 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages={"com.dm.estore.controllers"})
public class WebModule extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        // registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /*registry.addResourceHandler("/s*//*").addResourceLocations("classpath:/META-INF/webapp/WEB-INF/view/scripts*//*");
        registry.addResourceHandler("/c*//*").addResourceLocations("classpath:/META-INF/webapp/WEB-INF/view/css*//*");
        registry.addResourceHandler("/i*//*").addResourceLocations("classpath:/META-INF/webapp/WEB-INF/view/images*//*");
        registry.addResourceHandler("/WEB-INF/view*//*").addResourceLocations("classpath:/META-INF/webapp/WEB-INF/view*//*");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/webapp/WEB-INF/view/images/favicon.ico");*/
    }

    /*@Bean
    public ViewResolver viewResolver()
    {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }*/
}
