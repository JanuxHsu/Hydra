package hydra.hydra.gui;

import hydra.hydra.core.HydraController;
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

	public abstract void updateIsWorkerActive(boolean isWorking);

	public abstract void updateIsServerConnected(boolean isConnected);

	public abstract void updateMemoryUsages(Long freeMem, Long totalMem);

	public abstract void updateSystemInfo(SystemInfo systemInfo);

	// public abstract void updateZolaStatus(String state, String status);
}
