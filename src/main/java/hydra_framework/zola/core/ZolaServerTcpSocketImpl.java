package hydra_framework.zola.core;

import java.util.concurrent.ThreadPoolExecutor;

import hydra_framework.repository.ZolaServerRepository;

public class ZolaServerTcpSocketImpl extends ZolaServer {

	public ZolaServerTcpSocketImpl(ZolaController zolaController) {
		super(zolaController);
	}

	@Override
	public void open() {

		ThreadPoolExecutor threadPoolExecutor = this.zolaController.threadPoolExecutor;
		threadPoolExecutor.execute((new ZolaConnector(this.zolaController)));

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
