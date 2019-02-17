package hydra;


import hydra.server.core.HydraServer;
import hydra.server.core.HydraServerTcpSocketImpl;
import hydra.server.gui.HydraServerGui;
import hydra.server.gui.HydraServerSwingGui;

public class Main {

	public Main() {

	}
	
	public static void main(String[] args) {
		
		HydraServerGui gui = new HydraServerSwingGui();
		HydraServer hydraServer = new HydraServerTcpSocketImpl("Hydra Server", gui);
		hydraServer.open();

	}
}
