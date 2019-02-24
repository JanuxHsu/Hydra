package hydra.hydra.core;

public abstract class HydraClient {

	public HydraClient() {
		// TODO Auto-generated constructor stub
	}

	public abstract boolean open();

	public abstract void close();

	public abstract void broadcast(String message);

	public abstract void sendMessage(String text);

}
