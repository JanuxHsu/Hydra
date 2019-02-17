package hydraServerGui;

public interface HydraServerGuiAction {

	public void show();

	public void hide();

	public void close();

	public void writeLog(String logText);

	public void refreshClientPanel();
}
