package hydra.zola.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import hydra.zola.serviceServlets.HelloServlet;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		ServerSocket serverSocket = null;
//		try {
//
//			int port = this.zolaController.httpServicePort;
//
//			serverSocket = new ServerSocket(port);
//
//			this.zolaController.setWebServerInfo(port);
//			// Now enter an infinite loop, waiting for & handling connections.
//			while (true) {
//				// Wait for a client to connect. The method will block;
//				// when it returns the socket will be connected to the client
//				Socket client = serverSocket.accept();
//
//				// Get input and output streams to talk to the client
//				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//				PrintWriter out = new PrintWriter(client.getOutputStream());
//
//				// Start sending our reply, using the HTTP 1.1 protocol
//				out.print("HTTP/1.1 200 \r\n"); // Version & status code
//				out.print("Content-Type: application/json\r\n"); // The type of data
//				out.print("Connection: close\r\n"); // Will close stream
//				out.print("\r\n"); // End of headers
//
//				String line = in.readLine();
//
//				try {
//					String[] tokens = line.split(" ");
//
//					String method = tokens[0];
//					String req_path = tokens[1];
//
//					HttpPathHandler handler = new HttpPathHandler(this.zolaController.zolaServerRepository, method,
//							req_path);
//					out.println(handler.getResponse());
//
//				} catch (Exception e) {
//					e.printStackTrace();
//
//				} finally {
//					// Close socket, breaking the connection to the client, and
//					// closing the input and output streams
//					out.close(); // Flush and close the output stream
//					in.close(); // Close the input stream
//					client.close(); // Close the socket itself
//				}
//
//			}
//		}
//		// If anything goes wrong, print an error message
//		catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Usage: java HttpMirror <port>");
//		} finally {
//
//			if (serverSocket != null) {
//				try {
//					serverSocket.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//		}
	}

}
