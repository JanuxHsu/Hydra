package hydra.viper.core;

import hydra.viper.gui.ViperClientGui;
import hydra.viper.gui.ViperGuiController;

public abstract class ViperClient {

	protected ViperClientGui mainForm;

	ViperGuiController viperGuiController;

	public ViperClient(String clientName, ViperClientGui gui) {

		this.mainForm = gui;
		this.mainForm.setTitle(clientName);
		this.mainForm.show();
	}

	public abstract void open() throws Exception;

	public abstract void close();

	public abstract void sendMessage(String messageText);

}
