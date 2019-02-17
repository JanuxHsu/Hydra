package hydra.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hydra.server.model.HydraConnectionClient;

public class HydraServerRepository {

	public static Boolean isRunSocketServer = true;
	ConcurrentHashMap<String, HydraConnectionClient> HydraClients = new ConcurrentHashMap<>();
	ExecutorService threadPool = Executors.newCachedThreadPool();

	public HydraServerRepository() {
		// TODO Auto-generated constructor stub
	}

	public ExecutorService getThreadPool() {
		return this.threadPool;
	}

	public ConcurrentHashMap<String, HydraConnectionClient> getClients() {
		return this.HydraClients;

	}

	public String addClient(HydraConnectionClient client) {

		this.HydraClients.put(client.clientID, client);
		return client.clientID;
	}

}
