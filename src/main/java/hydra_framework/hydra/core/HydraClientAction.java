package hydra_framework.hydra.core;

public interface HydraClientAction {
	public void open();

	public void close();

	public void broadcast(String message);
}
