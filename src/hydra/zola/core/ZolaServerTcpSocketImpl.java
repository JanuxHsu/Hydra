package hydra.zola.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.model.HydraConnectionClient;

public class ZolaServerTcpSocketImpl extends ZolaServer {

	public static final int LISTEN_PORT = 5987;
	ExecutorService threadPool = this.getRepository().getThreadPool();

	public ZolaServerTcpSocketImpl(String serverName, ZolaServerGui gui) {
		super(serverName, gui);
	}

	public void listenRequest() throws IOException {

		ServerSocket serverSocket = null;

		serverSocket = new ServerSocket(LISTEN_PORT);

		this.mainForm.writeLog("Server listening requests...");

		while (ZolaServerRepository.isRunSocketServer) {
			Socket socket = serverSocket.accept();
			// String client_id = this.addClient(socket);

			HydraConnectionClient client = this.addClient(socket);
			threadPool.execute(client.clientThread);

		}

		serverSocket.close();
		this.mainForm.writeLog("Server Closed.");

	}

	@Override
	public void open() {
		try {
			listenRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		ZolaServerRepository.isRunSocketServer = false;

	}

	@Override
	public void broadcast(String requestClient, String message) {

		System.out.println("Request boardcast: " + message);
		ConcurrentHashMap<String, HydraConnectionClient> clients = this.hydraRepository.getClients();

		for (String client_id : clients.keySet()) {
			HydraConnectionClient client = clients.get(client_id);
			try {
				client.clientThread.sendMessage(requestClient +" Send " + message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	

}
