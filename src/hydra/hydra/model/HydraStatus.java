package hydra.hydra.model;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class HydraStatus {

	boolean isConnectedToServer = false;

	Date lastAckTime = Calendar.getInstance().getTime();
	String serverResponse;
	String connectionInfo = null;

	Map<String, Future<String>> workerJobs = new LinkedHashMap<>();

	public HydraStatus() {
		// this.maxMemory = Runtime.getRuntime().totalMemory();
	}

	public void setServerLastResponse(String message) {
		this.serverResponse = message;
	}

	public boolean isConnectedToServer() {

		serverResponse = serverResponse == null ? "" : serverResponse;
		// System.out.println("b");
		return isConnectedToServer && !serverResponse.isEmpty();
	}

	public void setConnectedToServer(boolean isConnectedToServer) {
		this.isConnectedToServer = isConnectedToServer;
	}

	public String getConnectionInfo() {

		return this.connectionInfo;
	}

	public void setConnectionInfo(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public void setLastAckTime() {
		this.lastAckTime = Calendar.getInstance().getTime();

	}

	public Date getLastAckTime() {
		return this.lastAckTime;

	}

	public void addWorkerCurrentFuture(String job_id, Future<String> job) {
		this.workerJobs.put(job_id, job);
	}

	public Map<String, Future<String>> getWorkerFutureMap() {
		return this.workerJobs;
	}

	public Future<String> getWorkerCurrentFuture(String job_id) {
		return this.workerJobs.get(job_id);
	}

}
