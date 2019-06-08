package hr.fer.zemris.java.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener used for initializing server start time so the server uptime can
 * be calculated on demand. It maps current time in miliseconds in
 * global attributes to "servletStartTime" key.
 * 
 * @author Filip Husnjak
 */
@WebListener
public class ServerUpTimeListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("serverStartTime", System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("serverStartTime");
	}

}
