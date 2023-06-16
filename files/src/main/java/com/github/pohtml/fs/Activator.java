package com.github.pohtml.fs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Activator implements ServletContextListener {

	private final boolean redirecting;
	
	private ServletContext context; 
	
	public Activator() {
		this.redirecting = System.getProperty("com.github.pohtml.fs.base") != null;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		if (redirecting) {
			context.addServlet("redirect", Redirect.class).addMapping("/*");	
		}
	}
	
}