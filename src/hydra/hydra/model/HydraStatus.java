package hydra.hydra.model;

public class HydraStatus {

	boolean isConnectedToServer = false;
	String serverResponse;
	String connectionInfo = null;
	boolean isWorkerActive = false;

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

	public boolean isWorkerActive() {
		return isWorkerActive;
	}

	public void setWorkerActive(boolean isWorkerActive) {
		this.isWorkerActive = isWorkerActive;
	}

}
