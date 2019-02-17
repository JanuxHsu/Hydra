package HydraClientCore;

public interface HydraClientAction {
	public void open();

	public void close();

	public void broadcast(String message);
}
