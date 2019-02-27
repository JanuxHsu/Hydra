package hydra.hydra.model;

public class HydraStatus {

	boolean isConnectedToServer = false;
	String connectionInfo = null;
	boolean isWorkerActive = false;

	public boolean isConnectedToServer() {
		
	//System.out.println("b");
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

	public HydraStatus() {

	}

}
