package com.github.pohtml.fs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;

@WebListener
public class Initializer implements ServletContextListener {

	private final boolean redirecting;
	
	private ServletContext context; 
	
	public Initializer() {
		this.redirecting = System.getProperty("com.github.pohtml.fs.base") != null;
	}
	
	private void addServlet(Class<? extends HttpServlet> code, String mapping) {
		context.addServlet(code.getSimpleName(), code).addMapping(mapping);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		if (redirecting) {
			addServlet(Redirect.class, "/*");	
		}
	}
	
}