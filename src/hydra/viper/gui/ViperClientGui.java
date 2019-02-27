package hydra.viper.gui;

import javax.swing.JFrame;

import hydra.viper.core.ViperController;

public abstract class ViperClientGui {
	public static enum connectionBtnState {
		Connect, Disconnect, Conncecting
	};

	ViperController viperController;
	protected JFrame mainWindow;

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
