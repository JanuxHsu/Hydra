package hydra.viper.core;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ViperClientTcpSocketImpl extends ViperClient {

	boolean isRun = true;
	ExecutorService executorService = Executors.newCachedThreadPool();

	ViperConnector viperConnector;

	ArrayList<Future<?>> jobHist = new ArrayList<>();

	public ViperClientTcpSocketImpl(ViperController viperController) {
		super(viperController);
	}

	@Override
	public void open() {
		this.viperConnector = new ViperConnector(isRun);
		Future<?> jobResult = executorService.submit(viperConnector);

		jobHist.add(jobResult);
		int count = 0;
		for (Future<?> jobRes : jobHist) {
			//System.out.println(count + " | " + jobRes.isDone());
			count++;
		}

	}

	@Override
	public void close() {

		System.out.println("Closing");
		this.viperConnector.shutDown();

	}

	@Override
	public void sendMessage(String messageText) {
		System.out.println("Message: " + messageText);

	}

}
