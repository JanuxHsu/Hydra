package hydra.server.gui;

import java.util.List;

public interface ZolaServerGui {

	public void show();

	public void hide();

	public void close();

	public void writeLog(String logText);

	void refreshTable(List<Object[]> objects);

	void setTitle(String name);
}
