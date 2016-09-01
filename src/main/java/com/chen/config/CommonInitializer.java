package com.chen.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

@Order(1)
public class CommonInitializer implements WebApplicationInitializer {
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		servletContext.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
		
		servletContext.addListener(ContextLoaderListener.class);
		
		DispatcherServlet dispatcher = new DispatcherServlet();
		ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcher", dispatcher);
		dynamic.setInitParameter("contextConfigLocation", "classpath:servlet.xml");
		dynamic.setLoadOnStartup(2);
		dynamic.addMapping("/");
	}

}
