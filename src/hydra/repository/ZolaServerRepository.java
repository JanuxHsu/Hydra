package hydra.repository;

import java.util.concurrent.ConcurrentHashMap;

import hydra.zola.model.HydraConnectionClient;

public class ZolaServerRepository {

	public static Boolean isRunSocketServer = true;
	ConcurrentHashMap<String, HydraConnectionClient> HydraClients = new ConcurrentHashMap<>();

	public ZolaServerRepository() {
		// TODO Auto-generated constructor stub
	}

	public ConcurrentHashMap<String, HydraConnectionClient> getClients() {
		return this.HydraClients;

	}

	public String addClient(HydraConnectionClient client) {

		this.HydraClients.put(client.getClientID(), client);
		return client.getClientID();
	}

}
