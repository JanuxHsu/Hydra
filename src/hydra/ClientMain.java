package hydra;

import hydra.hydra.core.HydraClient;
import hydra.hydra.core.HydraClientTcpSocketImpl;
import hydra.hydra.core.HydraConfig;
import hydra.hydra.core.HydraController;
import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.core.ViperConfig;
import hydra.viper.core.ViperController;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {

//		ViperConfig viperConfig = new ViperConfig();
//		viperConfig.setGUI_type(ViperConfig.Swing);
//
//		ViperController viperController = new ViperController(viperConfig);
//		ViperClient viperClient = new ViperClientTcpSocketImpl(viperController);
//		//viperClient.open();
//		viperController.connectToTarget();
//		viperClient.registerClient();

		HydraConfig hydraConfig = new HydraConfig();
		hydraConfig.setGUI_type(HydraConfig.Swing);

		HydraController hydraController = new HydraController(hydraConfig);
		HydraClient hydraClient = new HydraClientTcpSocketImpl(hydraController);

		hydraController.registerClient();

	}

}
