package hydra.hydra.gui;

import java.awt.TrayIcon.MessageType;
import java.util.List;

import com.google.gson.JsonObject;

import hydra.hydra.core.HydraController;
import hydra.hydra.gui.HydraClientSwingGui.IconMessageMode;
import oshi.SystemInfo;

public abstract class HydraClientGui {

	HydraController hydraController;

	public HydraClientGui(HydraController hydraController) {
		this.hydraController = hydraController;
		this.hydraController.setClientGui(this);
	}

	public abstract void show();

	public abstract void hide();

	public abstract void close();

	public abstract void setTitle(String title);

	public abstract void resetInputState();

	public abstract String getAutoCompleteKeyword();

	public abstract void displaySystemLog(String line);

	public abstract void updateConnectionStatus(String status);

	public abstract void updateWorkerStatus(boolean isWorking, int queuedJobs);

	public abstract void updateIsServerConnected(boolean isConnected);

	public abstract void updateMemoryUsages(Long freeMem, Long totalMem);

	public abstract void updateSystemInfo(SystemInfo systemInfo);

	public abstract void displayIconMessage(String caption, String message, MessageType messageType,
			IconMessageMode mode);

	public abstract void updateClientInfo(JsonObject clientInfoJson);

	public abstract void refreshTable(List<List<String>> rowList);

	// public abstract void updateZolaStatus(String state, String status);
}
