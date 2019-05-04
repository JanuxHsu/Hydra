package hydra.zola.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import oshi.util.FormatUtil;

public class ZolaController {

	final int servicePort;
	final int httpServicePort;

	protected ZolaServer serverCore;
	protected ZolaServerGui serverGui;
	ZolaServerRepository zolaServerRepository;

	ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);

	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 100, 5000, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>());

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonParser jsonParser = new JsonParser();

	public ZolaController(ZolaConfig zolaConfig) {

		this.zolaServerRepository = new ZolaServerRepository();
		this.serverGui = new ZolaServerSwingGui(this);

		this.servicePort = zolaConfig.servicePort;
		this.httpServicePort = zolaConfig.httpServicePort;

		this.setGuiTitle(zolaConfig.app_name);

		this.scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
			refreshPanel();
		}, 0, 1, TimeUnit.SECONDS);

		this.scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
			syslog("Clearing Idle Client threads...");
			try {
				clearIdleConnection();
				syslog("Clearing Idle Client threads...Completed.");
			} catch (Exception e) {
				syslog("Clearing Idle Client threads...Failed. Reason: " + e.getMessage());
			}

		}, 20, 10, TimeUnit.SECONDS);

		this.threadPoolExecutor.allowCoreThreadTimeOut(true);
		this.threadPoolExecutor.execute(new ZolaHttpService(this));

		ZolaHelper zolaHelper = ZolaHelper.getInstance();
		zolaHelper.setZolaController(this);
	}

	private void clearIdleConnection() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = this.zolaServerRepository.getClients();

		for (HydraConnectionClient client : clients.values()) {
			if (client.getLastUpdateTime() == null) {

				this.removeClient(client.getClientID());

			} else {
				long lapsed = Calendar.getInstance().getTime().getTime() - client.getLastUpdateTime().getTime();

				if (lapsed > 20000) {

					this.removeClient(client.getClientID());

				}
			}

		}

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

	public void refreshPanel() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaServerRepository.getClients();
		List<Object[]> rowList = new ArrayList<>();
		int count = 0;

		String[] keys = new ArrayList<>(clients.keySet()).toArray(new String[] {});

		Arrays.sort(keys);

		for (String clientId : keys) {
			count++;
			HydraConnectionClient client = clients.get(clientId);

			String displayMsg;
			try {
				JsonParser parser = new JsonParser();
				JsonObject messageJson = parser.parse(client.getMessage()).getAsJsonObject();

				messageJson = messageJson.get("message").getAsJsonObject();

				String cpu = messageJson.get("cpu").getAsString();
				String memory = messageJson.get("memory").getAsString();

				JsonObject networkJson = messageJson.get("network").getAsJsonObject();
				String bytesRecv = networkJson.get("totalBytesRecvDelta").getAsString();
				String bytesSent = networkJson.get("totalBytesSentDelta").getAsString();
				String totalErr = networkJson.get("totalNetworkErr").getAsString();

				Integer heartBeatInterval = messageJson.get("interval").getAsInt();

				displayMsg = String.format("CPU: %s%%, Memory: %s%%, Recv: %s/s, Sent: %s/s, Error: %s", cpu, memory,
						FormatUtil.formatBytes(Long.parseLong(bytesRecv) / heartBeatInterval),
						FormatUtil.formatBytes(Long.parseLong(bytesSent) / heartBeatInterval), totalErr);
			} catch (Exception e) {

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

		String threadStatus = String.format("Active/PoolSize : [%s/%s]", this.threadPoolExecutor.getActiveCount(),
				this.threadPoolExecutor.getCorePoolSize());
		this.serverGui.updateThreadPoolStatus(threadStatus);

	}

	public void updateClientMessage(String clientId, String message) {

		try {
			JsonElement clientMessage = jsonParser.parse(message);

			if (clientMessage.isJsonObject()) {

				HydraMessage hydraMessage = gson.fromJson(clientMessage, HydraMessage.class);
				MessageType messageType = hydraMessage.getMessageType();

				HydraConnectionClient client = this.zolaServerRepository.getClients().get(clientId);
				switch (messageType) {
				case REALTIMEINFO:
					client.updateLastMessage(message);
					JsonObject ack_json = new JsonObject();
					ack_json.addProperty("status", "received");
					HydraMessage ack_msg = new HydraMessage(ack_json, client.getClientID(), MessageType.ACKNOWLEDGE);

					client.getClientThread().sendMessage(ack_msg.toString());

					break;

				case REGISTER:
					JsonObject clientConfigjson = hydraMessage.getMessageBody().getAsJsonObject();
					client.setClientInfo(ClientType.valueOf(clientConfigjson.get("clientType").getAsString()),
							clientConfigjson.get("clientVersion").getAsString());
					break;

				case FULLSYSINFO:
					JsonObject fullInfo = hydraMessage.getMessageBody().getAsJsonObject();
					// System.out.println(gson.toJson(fullInfo));
					client.setClientSystemInfo(gson.toJson(fullInfo));
					break;

				default:

					System.out.println(hydraMessage.getMessageBody());
					break;
				}
			}

		} catch (Exception e) {
			System.out.println("Unknown type of message!");
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
		client.getClientThread().close();
		this.syslog(String.format("%s Disconnected!", client.getClientID()));

		if (this.threadPoolExecutor.getActiveCount() + 1 < this.threadPoolExecutor.getCorePoolSize()) {
			this.threadPoolExecutor.setCorePoolSize(this.threadPoolExecutor.getCorePoolSize() - 1);
		}

		// refreshPanel();
	}

	public void addClient(Socket socket) {
		String clientId = UUID.randomUUID().toString();
		RequestThread clientThread = new RequestThread(this, clientId, socket);
		HydraConnectionClient hydraClient = new HydraConnectionClient(clientId, "unknown", clientThread,
				Calendar.getInstance().getTime(), socket.getInetAddress());
		clientId = zolaServerRepository.addClient(hydraClient);

		// refreshPanel();
		this.threadPoolExecutor.execute(hydraClient.getClientThread());
		if (this.threadPoolExecutor.getActiveCount() + 1 > this.threadPoolExecutor.getCorePoolSize()) {
			this.threadPoolExecutor.setCorePoolSize(this.threadPoolExecutor.getCorePoolSize() + 2);
		}

		// this.exeexecute(hydraClient.getClientThread());

	}

	public void setWebServerInfo(int port) {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

			e.printStackTrace();
			host = "Unknown";
		}
		this.serverGui.setServiceInfo("HTTP API : " + host + ":" + port);
		this.serverGui.updateHttpServiceStatus("Jetty Server Started");
	}

	public ZolaServerRepository getRepository() {
		return this.zolaServerRepository;
	}

	public void shutDownAllClient() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaServerRepository.getClients();
		for (String clientId : clients.keySet()) {
			HydraConnectionClient client = clients.get(clientId);
			client.disconnect(true);
		}
	}

}
