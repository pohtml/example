package com.github.pohtml.classes;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;

@WebListener
public abstract class Initializer implements ServletContextListener {

	protected ServletContext context; 
	
	protected abstract void init();
	
	protected void addServlet(Class<? extends HttpServlet> code, String mapping) {
		context.addServlet(code.getSimpleName(), code).addMapping(mapping);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
	}
	
}