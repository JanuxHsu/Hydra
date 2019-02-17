package HydraServerCore;

public interface HydraServerAction {
	public void open();

	public void close();

	public void broadcast(String message);
}
