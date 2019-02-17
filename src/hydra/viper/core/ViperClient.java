package hydra.viper.core;

import hydra.viper.gui.ViperClientGui;

public abstract class ViperClient {

	protected ViperClientGui mainForm;

	public ViperClient(String clientName, ViperClientGui gui) {

		this.mainForm = gui;
		this.mainForm.setTitle(clientName);
		this.mainForm.show();
	}

	public abstract void open();

	public abstract void close();

	public abstract void broadcast(String message);

}
