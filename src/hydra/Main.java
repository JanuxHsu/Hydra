package hydra;

import hydra.zola.core.ZolaServer;
import hydra.zola.core.ZolaServerTcpSocketImpl;
import hydra.zola.gui.ZolaServerGui;
import hydra.zola.gui.ZolaServerSwingGui;

public class Main {

	public Main() {

	}

	public static void main(String[] args) {

		ZolaServerGui gui = new ZolaServerSwingGui();
		ZolaServer hydraServer = new ZolaServerTcpSocketImpl("Hydra Server", gui);
		hydraServer.open();

	}
}
