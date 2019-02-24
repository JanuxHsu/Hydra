package hydra.hydra.gui;

import hydra.viper.gui.ViperClientGui.connectionBtnState;

public abstract class HydraClientGui {

	public HydraClientGui() {
		// TODO Auto-generated constructor stub
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

	public void displaySystemLog(String line) {
		// TODO Auto-generated method stub

	}

}
