package HydraServerCore;

import java.net.Socket;
import java.util.Calendar;
import java.util.UUID;

import hydraServerGui.HydraServerGui;
import hydraServerGui.HydraServer_SwingGUI;
import model.HydraConnectionClient;
import repository.HydraServerRepository;

public abstract class HydraServer implements HydraServerAction {
	public String serverName;
	HydraServerGui mainForm;
	HydraServerRepository hydraRepository = new HydraServerRepository();

	public HydraServer(String serverName) {

		this.serverName = serverName;
		this.mainForm = new HydraServer_SwingGUI(this);
		this.mainForm.show();
	}

	public HydraConnectionClient addClient(Socket socket) {
		String clientId = UUID.randomUUID().toString();
		RequestThread clientThread = new RequestThread(this, clientId, socket);
		HydraConnectionClient hydraClient = new HydraConnectionClient(clientId, clientThread,
				Calendar.getInstance().getTime());
		clientId = this.hydraRepository.addClient(hydraClient);

		this.mainForm.refreshClientPanel();
		return hydraClient;
	}

	public HydraServerRepository getRepository() {
		return this.hydraRepository;
	}

	public void updateClientMessage(String clientId, String message) {
		this.getRepository().getClients().get(clientId).updateLastMessage(message);
		this.mainForm.refreshClientPanel();
	}

	public void removeClient(String clientId) {
		HydraConnectionClient client = this.getRepository().getClients().remove(clientId);
		this.mainForm.writeLog(String.format("%s終止連線!", client.clientID));

		this.mainForm.refreshClientPanel();
	}

}
