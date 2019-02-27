package hydra.hydra.gui;

import hydra.hydra.core.HydraController;
import hydra.viper.gui.ViperClientGui.connectionBtnState;

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

	public void setConnectionBtnState(connectionBtnState connectState) {
		// TODO Auto-generated method stub

	}

	public abstract void displaySystemLog(String line);

	public abstract void updateConnectionStatus(String status);

	public abstract void updateIsServerConnected(boolean isConnected);

	// public abstract void updateZolaStatus(String state, String status);
}
