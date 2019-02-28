package hydra.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hydra.zola.model.HydraConnectionClient;

public class ZolaServerRepository {

	public static Boolean isRunSocketServer = true;
	ConcurrentHashMap<String, HydraConnectionClient> HydraClients = new ConcurrentHashMap<>();
	ExecutorService threadPool = Executors.newCachedThreadPool();

	public ZolaServerRepository() {
		// TODO Auto-generated constructor stub
	}

	public ExecutorService getThreadPool() {
		return this.threadPool;
	}

	public ConcurrentHashMap<String, HydraConnectionClient> getClients() {
		return this.HydraClients;

	}

	public String addClient(HydraConnectionClient client) {

		this.HydraClients.put(client.getClientID(), client);
		return client.getClientID();
	}

}
