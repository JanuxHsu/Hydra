package HydraClientGui;

public interface HydraClientGuiAction {
	public void show();

	public void hide();

	public void close();

	public void writeLog(String logText);

	public void refreshClientPanel();
}
