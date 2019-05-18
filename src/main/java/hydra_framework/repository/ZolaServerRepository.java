package hydra_framework.repository;

import java.util.concurrent.ConcurrentHashMap;

import hydra_framework.zola.model.HydraConnectionClient;

public class ZolaServerRepository {

	public static Boolean isRunSocketServer = true;
	ConcurrentHashMap<String, HydraConnectionClient> HydraClients = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, String> table_clientID_map = new ConcurrentHashMap<>();

	public ZolaServerRepository() {

	}

	public ConcurrentHashMap<String, HydraConnectionClient> getClients() {
		return this.HydraClients;

	}

	public ConcurrentHashMap<String, String> getTableMap() {
		return this.table_clientID_map;

	}

	public String addClient(HydraConnectionClient client) {

		this.HydraClients.put(client.getClientID(), client);
		return client.getClientID();
	}

}
