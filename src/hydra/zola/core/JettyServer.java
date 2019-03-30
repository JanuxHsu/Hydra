package hydra.zola.core;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyServer {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);
		ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);

		servletContextHandler.setContextPath("/");
		server.setHandler(servletContextHandler);
		ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter("jersey.config.server.provider.packages", "hydra.zola.serviceServlets");
		// ServletHandler handler = new ServletHandler();

		// server.setHandler(handler);

		// handler.addServletWithMapping(HelloServlet.class, "/status/*");

		server.start();

		// server.join();
	}

}
