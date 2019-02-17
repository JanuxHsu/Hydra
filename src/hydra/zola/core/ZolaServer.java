package hydra.zola.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.model.HydraConnectionClient;

public abstract class ZolaServer {
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

	public abstract void broadcast(String message);

	public HydraConnectionClient addClient(Socket socket) {
		String clientId = UUID.randomUUID().toString();
		RequestThread clientThread = new RequestThread(this, clientId, socket);
		HydraConnectionClient hydraClient = new HydraConnectionClient(clientId, clientThread,
				Calendar.getInstance().getTime());
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
		this.mainForm.writeLog(String.format("%s終止連線!", client.clientID));

		refreshPanel();
	}

	public void refreshPanel() {
		ConcurrentHashMap<String, HydraConnectionClient> clients = hydraRepository.getClients();
		List<Object[]> rowList = new ArrayList<>();
		int count = 0;
		for (String clientId : clients.keySet()) {
			count++;
			HydraConnectionClient client = clients.get(clientId);
			rowList.add(new Object[] { count, clientId, client.getFormattedAcceptTime(), client.getMessage() });
		}
		this.mainForm.refreshTable(rowList);
	}

}
