package hydra.zola.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.model.HydraConnectionClient;
import hydra.zola.model.HydraConnectionClient.ClientType;

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

		if (message.toLowerCase().equals("ls")) {

			ConcurrentHashMap<String, HydraConnectionClient> clients = this.getRepository().getClients();
			JsonArray statusJson = new JsonArray();

			for (String client_id : clients.keySet()) {
				JsonObject statusRes = new JsonObject();

				HydraConnectionClient connected_client = clients.get(client_id);
				if (connected_client.getClientType().equals(ClientType.UNKNOWN)) {
					statusRes.addProperty("server", connected_client.getClientAddress().getHostName());
					statusRes.addProperty("status", connected_client.getMessage());
					statusJson.add(statusRes);
				}

			}

			this.broadcast(clientId, new Gson().toJson(statusJson));

		} else {
			this.getRepository().getClients().get(clientId).updateLastMessage(message);
			refreshPanel();
		}

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

			String displayMsg;
			try {
				JsonParser parser = new JsonParser();
				JsonObject messageJson = parser.parse(client.getMessage()).getAsJsonObject();

				messageJson = messageJson.get("message").getAsJsonObject();

				String cpu = messageJson.get("cpu").getAsString();
				String memory = messageJson.get("memory").getAsString();
				displayMsg = String.format("CPU: %s%%, Memory: %s%%", cpu, memory);
			} catch (Exception e) {
				e.printStackTrace();
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
