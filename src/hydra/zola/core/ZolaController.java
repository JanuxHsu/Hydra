package hydra.zola.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hydra.model.HydraMessage;
import hydra.model.HydraMessage.MessageType;
import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.gui.ZolaServerSwingGui;
import hydra.zola.model.HydraConnectionClient;
import hydra.zola.model.HydraConnectionClient.ClientType;

public class ZolaController {

	final int servicePort;
	final int httpServicePort;

	protected ZolaServer serverCore;
	protected ZolaServerGui serverGui;
	ZolaServerRepository zolaServerRepository;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonParser jsonParser = new JsonParser();

	public ZolaController(ZolaConfig zolaConfig) {

		this.zolaServerRepository = new ZolaServerRepository();
		this.serverGui = new ZolaServerSwingGui(this);

		this.servicePort = zolaConfig.servicePort;
		this.httpServicePort = zolaConfig.httpServicePort;

		this.setGuiTitle(zolaConfig.app_name);
	}

	private void setGuiTitle(String app_name) {
		this.serverGui.setTitle(app_name);

	}

	public void showGui() {

		this.serverGui.show();

	}

	public void setServerCore(ZolaServer zolaServer) {
		this.serverCore = zolaServer;
	}

	public void syslog(String log) {
		this.serverGui.writeLog(log);

	}

	public void addClient(Socket socket) {
		String clientId = UUID.randomUUID().toString();
		RequestThread clientThread = new RequestThread(this, clientId, socket);
		HydraConnectionClient hydraClient = new HydraConnectionClient(clientId, "unknown", clientThread,
				Calendar.getInstance().getTime(), socket.getInetAddress());
		clientId = zolaServerRepository.addClient(hydraClient);

		refreshPanel();

		this.zolaServerRepository.getThreadPool().execute(hydraClient.getClientThread());

	}

	public void refreshPanel() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaServerRepository.getClients();
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
				// e.printStackTrace();
				displayMsg = client.getMessage();
			}

			ArrayList<String> tableData = new ArrayList<>();

			tableData.add(Integer.toString(count));
			tableData.add(client.getClientAddress().getHostName());
			tableData.add(client.getClientVersion());
			tableData.add(client.getClientAddress().getHostAddress());
			tableData.add(client.getFormattedAcceptTime());
			tableData.add(displayMsg);

			rowList.add(tableData.toArray());
		}

		this.serverGui.refreshTable(rowList);

	}

	public void updateClientMessage(String clientId, String message) {

		try {
			JsonElement clientMessage = jsonParser.parse(message);

			if (clientMessage.isJsonObject()) {

				HydraMessage hydraMessage = gson.fromJson(clientMessage, HydraMessage.class);
				MessageType messageType = hydraMessage.getMessageType();

				HydraConnectionClient client = this.zolaServerRepository.getClients().get(clientId);
				switch (messageType) {
				case HEARTBEAT:
					client.updateLastMessage(message);

					break;

				case REGISTER:
					JsonObject infoJson = hydraMessage.getMessageBody().getAsJsonObject();

					ClientType clientType = ClientType.valueOf(infoJson.get("clientType").getAsString());
					String clientVersion = infoJson.get("clientVersion").getAsString();
					client.setClientInfo(clientType, clientVersion);
					break;

				default:
					break;
				}
			}
			refreshPanel();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void broadcast(String requestClient, String message) {

		System.out.println("Request boardcast: " + message);
		ConcurrentHashMap<String, HydraConnectionClient> clients = this.zolaServerRepository.getClients();

		JsonObject broadcastJson = new JsonObject();

		broadcastJson.addProperty("broadcaster", requestClient);
		broadcastJson.addProperty("body", message);

		for (String client_id : clients.keySet()) {
			HydraConnectionClient client = clients.get(client_id);
			try {
				client.getClientThread().sendMessage(new Gson().toJson(broadcastJson));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void removeClient(String clientId) {
		HydraConnectionClient client = this.zolaServerRepository.getClients().remove(clientId);
		this.syslog(String.format("%s終止連線!", client.getClientID()));

		refreshPanel();
	}

	public void setWebServerInfo(int port) {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			host = "Unknown";
		}
		this.serverGui.setServiceInfo("Web Service : " + host + ":" + port);

	}

}
