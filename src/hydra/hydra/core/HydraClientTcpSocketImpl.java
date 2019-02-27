package hydra.hydra.core;

import java.util.concurrent.Future;

public class HydraClientTcpSocketImpl extends HydraClient {

	HydraConnector hydraConnector;

	public HydraClientTcpSocketImpl(HydraController hydraController) {
		super(hydraController);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean open() {
		this.hydraConnector = new HydraConnector(this.hydraController);
		Future<?> jobResult = this.hydraController.getExecutorPool().submit(this.hydraConnector);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return !jobResult.isDone();

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadcast(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String text) {
		if (this.hydraConnector != null) {
			hydraConnector.sendMessage(text);
			System.out.println("Message: " + text);
		}

	}


}
