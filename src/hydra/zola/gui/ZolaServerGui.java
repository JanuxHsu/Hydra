package hydra.zola.gui;

import java.util.List;

public interface ZolaServerGui {

	public void show();

	public void hide();

	public void close();

	public void writeLog(String logText);

	public void refreshTable(List<Object[]> objects);

	public void setTitle(String name);

	public void setServiceInfo(String infoText);
}
