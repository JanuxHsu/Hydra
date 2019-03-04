package hydra;

import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.core.ViperConfig;
import hydra.viper.core.ViperController;

public class ViperMain {
	public static void main(String[] args) {
		ViperConfig viperConfig = new ViperConfig();
		viperConfig.setGUI_type(ViperConfig.Swing);

		ViperController viperController = new ViperController(viperConfig);
		ViperClient viperClient = new ViperClientTcpSocketImpl(viperController);
		// viperClient.open();
		viperController.connectToTarget();
		viperClient.registerClient();
	}
}
