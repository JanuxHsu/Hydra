package hydra.hydra.model;

public class HydraStatus {

	boolean isConnectedToServer = false;
	String connectionInfo = null;
	boolean isWorkerActive = false;

	Long maxMemory;

	Long freeMemory;

	public HydraStatus() {
		//this.maxMemory = Runtime.getRuntime().totalMemory();
	}

	public boolean isConnectedToServer() {

		// System.out.println("b");
		return isConnectedToServer;
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

	public boolean isWorkerActive() {
		return isWorkerActive;
	}

	public void setWorkerActive(boolean isWorkerActive) {
		this.isWorkerActive = isWorkerActive;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}

}
