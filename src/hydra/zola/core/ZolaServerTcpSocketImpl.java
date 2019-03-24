package hydra.zola.core;

import java.util.concurrent.ExecutorService;

import hydra.repository.ZolaServerRepository;

public class ZolaServerTcpSocketImpl extends ZolaServer {

	public ZolaServerTcpSocketImpl(ZolaController zolaController) {
		super(zolaController);
	}

	@Override
	public void open() {

		ExecutorService executorService = this.zolaController.zolaServerRepository.getThreadPool();
		executorService.submit(new ZolaConnector(this.zolaController));
		
		executorService.submit(new ZolaHttpService(this.zolaController));

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
