package hydra.viper.gui;

import hydra.viper.core.ViperController;

public abstract class ViperClientGui {
	public static enum connectionBtnState {
		Connect, Disconnect, Conncecting
	};

	ViperController viperController;

	public ViperClientGui(ViperController controller) {
		this.viperController = controller;
		this.viperController.setClientGui(this);
	}

	public abstract void setTitle(String title);

	public abstract void show();

	public abstract void hide();

	public abstract void close();

	public abstract void displayMessage(String messages);

	public abstract void resetInputState();

	public abstract String getAutoCompleteKeyword();

	public abstract void setConnectionBtnState(connectionBtnState connectState);

	public abstract void displaySystemLog(String line);
}
