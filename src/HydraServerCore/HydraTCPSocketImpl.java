package HydraServerCore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import model.HydraConnectionClient;
import repository.HydraServerRepository;

public class HydraTCPSocketImpl extends HydraServer {

	public static final int LISTEN_PORT = 5987;
	ExecutorService threadPool = this.getRepository().getThreadPool();

	public HydraTCPSocketImpl(String serverName) {
		super(serverName);

	}

	public void listenRequest() throws IOException {

		ServerSocket serverSocket = null;

		serverSocket = new ServerSocket(LISTEN_PORT);

		this.mainForm.writeLog("Server listening requests...");

		while (HydraServerRepository.isRunSocketServer) {
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
		HydraServerRepository.isRunSocketServer = false;

	}

	@Override
	public void broadcast(String message) {

		System.out.println("Request boardcast: " + message);
		ConcurrentHashMap<String, HydraConnectionClient> clients = this.hydraRepository.getClients();

		for (String client_id : clients.keySet()) {
			HydraConnectionClient client = clients.get(client_id);
			try {
				client.clientThread.sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
