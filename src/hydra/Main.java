package hydra;


import hydra.server.core.ZolaServer;
import hydra.server.core.ZolaServerTcpSocketImpl;
import hydra.server.gui.ZolaServerGui;
import hydra.server.gui.ZolaServerSwingGui;

public class Main {

	public Main() {

	}
	
	public static void main(String[] args) {
		
		ZolaServerGui gui = new ZolaServerSwingGui();
		ZolaServer hydraServer = new ZolaServerTcpSocketImpl("Hydra Server", gui);
		hydraServer.open();

	}
}
