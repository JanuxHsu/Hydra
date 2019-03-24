package hydra.zola.core;

import hydra.repository.ZolaServerRepository;
import hydra.zola.gui.ZolaServerGui;

public abstract class ZolaServer {

	int listenPort = 5978;
	public String serverName;
	ZolaServerGui mainForm;
	ZolaServerRepository hydraRepository = new ZolaServerRepository();
	final ZolaController zolaController;

	public ZolaServer(ZolaController zolaController) {
		this.zolaController = zolaController;
		this.zolaController.setServerCore(this);
		this.zolaController.showGui();

	}

	public abstract void open();

	public abstract void close();

	public abstract void broadcast(String reqest_Client, String message2);

	public ZolaServerRepository getRepository() {
		return this.hydraRepository;
	}

	public void setPort(String serverPort) {
		this.listenPort = Integer.parseInt(serverPort);

	}

}
