package hydra.zola.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.model.HydraConnectionClient;

public abstract class ZolaServer {

	int listenPort = 5978;
	public String serverName;
	ZolaServerGui mainForm;
	ZolaServerRepository hydraRepository = new ZolaServerRepository();

	public ZolaServer(String serverName, ZolaServerGui gui) {
		this.serverName = serverName;
		this.mainForm = gui;
		this.mainForm.setTitle(serverName);

		this.mainForm.show();

	}

	public abstract void open();

	public abstract void close();

	public abstract void broadcast(String reqest_Client, String message2);

	public HydraConnectionClient addClient(Socket socket) {
		String clientId = UUID.randomUUID().toString();
		RequestThread clientThread = new RequestThread(this, clientId, socket);
		HydraConnectionClient hydraClient = new HydraConnectionClient(clientId, clientThread,
				Calendar.getInstance().getTime(), socket.getInetAddress());
		clientId = this.hydraRepository.addClient(hydraClient);

		refreshPanel();
		return hydraClient;
	}

	public ZolaServerRepository getRepository() {
		return this.hydraRepository;
	}

	public void updateClientMessage(String clientId, String message) {
		this.getRepository().getClients().get(clientId).updateLastMessage(message);
		refreshPanel();
	}

	public void removeClient(String clientId) {
		HydraConnectionClient client = this.getRepository().getClients().remove(clientId);
		this.mainForm.writeLog(String.format("%s終止連線!", client.getClientID()));

		refreshPanel();
	}

	public void refreshPanel() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = hydraRepository.getClients();
		List<Object[]> rowList = new ArrayList<>();
		int count = 0;
		for (String clientId : clients.keySet()) {
			count++;
			HydraConnectionClient client = clients.get(clientId);
			System.out.println(client.getClientAddress().getHostAddress());
			String displayMsg;
			try {
				JsonParser parser = new JsonParser();
				JsonObject messageJson = parser.parse(client.getMessage()).getAsJsonObject();
				
				System.out.println(messageJson.get("message").getAsString());

				JsonObject messagebody = parser.parse(messageJson.get("message").getAsString()).getAsJsonObject();

				
				
				String cpu = messagebody.get("cpu").getAsString();
				String memory = messagebody.get("memory").getAsString();
				displayMsg = String.format("CPU: %s%%, Memory: %s%%", cpu, memory);
			} catch (Exception e) {
				displayMsg = client.getMessage();
			}

//			String displayMsg = String.format("CPU: %s%%, Memory: %s%%", cpu, memory);

			rowList.add(new Object[] { count, client.getClientAddress().getHostName(),
					client.getClientAddress().getHostAddress(), client.getFormattedAcceptTime(), displayMsg });
		}
		this.mainForm.refreshTable(rowList);
	}

	public void setPort(String serverPort) {
		this.listenPort = Integer.parseInt(serverPort);

	}

}
