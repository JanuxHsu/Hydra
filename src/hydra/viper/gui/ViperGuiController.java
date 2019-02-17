package hydra.viper.gui;

public class ViperGuiController {

	protected ViperClientGui gui;

	public ViperGuiController(ViperClientGui gui) {
		this.gui = gui;
	}

	public void sendCommand(String command) {

		this.gui.displayMessage(command);

	}

}
