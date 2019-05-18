package hydra_framework.zola.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class ZolaHttpService implements Runnable {

	final ZolaController zolaController;

	public ZolaHttpService(ZolaController zolaController) {
		this.zolaController = zolaController;
	}

	@Override
	public void run() {
		int port = this.zolaController.httpServicePort;
		//
		this.zolaController.setWebServerInfo(port);

		Server server = new Server(port);

		ServletContextHandler servletContextHandler = new ServletContextHandler();

		servletContextHandler.setContextPath("/");
		server.setHandler(servletContextHandler);

		ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter("jersey.config.server.provider.packages", "hydra.zola.serviceServlets");

		try {
			server.start();
			server.join();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
