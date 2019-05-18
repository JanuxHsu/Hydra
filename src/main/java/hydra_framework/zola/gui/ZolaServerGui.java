package hydra_framework.zola.gui;

import java.util.List;

import hydra_framework.zola.model.HydraConnectionClient;

public interface ZolaServerGui {

	public void show();

	public void hide();

	public void close();

	public void writeLog(String logText);

	public void refreshTable(List<List<String>> rowList);

	public void setTitle(String name);

	public void setServiceInfo(String infoText);

	public void updateThreadPoolStatus(String status);

	public void updateHttpServiceStatus(String status);

	public void setupOperationPanel(HydraConnectionClient client);
}
