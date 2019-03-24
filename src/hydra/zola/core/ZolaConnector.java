package hydra.zola.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import hydra.repository.ZolaServerRepository;

public class ZolaConnector implements Runnable {

	final ZolaController zolaController;

	final int servicePort;

	public ZolaConnector(ZolaController zolaController) {

		this.zolaController = zolaController;
		this.servicePort = this.zolaController.servicePort;
	}

	@Override
	public void run() {

		ServerSocket serverSocket = null;

		try {

			serverSocket = new ServerSocket(servicePort);
			this.zolaController.syslog("Server listening requests on port : " + servicePort + ".");

			while (ZolaServerRepository.isRunSocketServer) {
				Socket socket = serverSocket.accept();

				// String client_id = this.addClient(socket);

				this.zolaController.addClient(socket);

			}

			serverSocket.close();
			this.zolaController.syslog("Server Closed.");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
