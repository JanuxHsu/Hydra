package hydra;

import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.core.ViperConfig;
import hydra.viper.core.ViperController;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {

		ViperConfig viperConfig = new ViperConfig();
		viperConfig.setGUI_type(ViperConfig.Swing);

		ViperController viperController = new ViperController(viperConfig);
		ViperClient viperClient = new ViperClientTcpSocketImpl(viperController);
		//viperClient.open();

	}

}
