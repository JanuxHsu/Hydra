package hydra.zola.core;

import hydra.repository.ZolaServerRepository;

public class ZolaServerTcpSocketImpl extends ZolaServer {

	public ZolaServerTcpSocketImpl(ZolaController zolaController) {
		super(zolaController);
	}

	@Override
	public void open() {

		System.out.println("adw");

		this.zolaController.zolaServerRepository.getThreadPool().submit(new ZolaConnector(this.zolaController));

	}

	@Override
	public void close() {
		ZolaServerRepository.isRunSocketServer = false;

	}

	@Override
	public void broadcast(String reqest_Client, String message2) {
		// TODO Auto-generated method stub

	}

}
