package hydra.viper.core;

import hydra.viper.gui.ViperClientGui;

public class ViperController {

	protected final ViperClient clientCore;
	protected final ViperClientGui clientGui;

	public ViperController(ViperClient viperClient, ViperClientGui viperClientGui) {
		this.clientCore = viperClient;
		this.clientGui = viperClientGui;
	}

}
