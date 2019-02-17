package hydra.client.core;

public interface HydraClientAction {
	public void open();

	public void close();

	public void broadcast(String message);
}
