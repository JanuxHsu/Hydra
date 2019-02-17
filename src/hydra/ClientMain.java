package hydra;

import hydra.viper.core.ViperClient;
import hydra.viper.core.ViperClientTcpSocketImpl;
import hydra.viper.gui.ViperClientGui;
import hydra.viper.gui.ViperClientSwingGui;

public class ClientMain {

	public ClientMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		ViperClientGui viperClientGui = new ViperClientSwingGui();
		ViperClient viperClient = new ViperClientTcpSocketImpl("Viper", viperClientGui);
		viperClient.open();

	}

}
