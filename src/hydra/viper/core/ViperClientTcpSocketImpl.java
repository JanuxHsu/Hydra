package hydra.viper.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ViperClientTcpSocketImpl extends ViperClient {

	ExecutorService executorService = Executors.newCachedThreadPool();

	ViperConnector viperConnector;

	public ViperClientTcpSocketImpl(ViperController viperController) {
		super(viperController);
	}

	@Override
	public boolean open() {

		this.viperConnector = new ViperConnector(this.viperController);
		Future<?> jobResult = executorService.submit(viperConnector);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return !jobResult.isDone();

	}

	@Override
	public void close() {

		this.viperConnector.shutDown();
		this.viperConnector = null;
	}

	@Override
	public void sendMessage(String messageText) {
		if (this.viperConnector != null) {
			viperConnector.sendMessage(messageText);
			System.out.println("Message: " + messageText);
		}

	}

	@Override
	public void registerClient() {
		if (this.viperConnector.socket != null) {
			this.sendMessage("I'm a Viper");
		}

	}

}
