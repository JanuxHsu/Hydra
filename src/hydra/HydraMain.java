package hydra;

import hydra.hydra.core.HydraClient;
import hydra.hydra.core.HydraClientTcpSocketImpl;
import hydra.hydra.core.HydraConfig;
import hydra.hydra.core.HydraController;

public class HydraMain {

	public HydraMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

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
		hydraConfig.app_name = "Hydra";

		HydraController hydraController = new HydraController(hydraConfig);
		HydraClient hydraClient = new HydraClientTcpSocketImpl(hydraController);

	}

}
