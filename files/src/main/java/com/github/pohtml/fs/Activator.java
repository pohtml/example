package com.github.pohtml.fs;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

@WebListener
public class Activator implements ServletContextListener {

	private final boolean redirecting;
	
	public Activator() {
		this.redirecting = System.getProperty("com.github.pohtml.fs.base") != null;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (redirecting) {
			Dynamic registration = event.getServletContext().addServlet("redirector", Redirector.class);
			registration.addMapping("/*");	
		}
	}
	
}
