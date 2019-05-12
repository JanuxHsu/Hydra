package hydra.hydra.model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.gson.JsonObject;

import hydra.hydra.core.WorkerTimeoutMonitor;
import hydra.hydra.listeners.WorkerListener;
import hydra.hydra.listeners.WorkerListener.WorkerStatus;

public class WorkerCallable implements Callable<String> {
	private final String job_id;
	private final List<String> commands;
	private int timeout = 300;
	private WorkerListener workerListener;

	public WorkerCallable(JsonObject commandObj) {
		this.job_id = commandObj.get("job_id").getAsString();
		commands = Arrays.asList(commandObj.get("command").getAsString().split(" "));
		this.timeout = commandObj.get("timeout").getAsInt();
		System.out.println(commands);
	}

	public void setWorkerListener(WorkerListener workerListener) {
		this.workerListener = workerListener;
	}

	public String getJobId() {

		return this.job_id;
	}

	@Override
	public String call() throws Exception {
		this.workerListener.doUpdateStatus(this.job_id, WorkerStatus.Start, "start");

		WorkerTimeoutMonitor workerTimeoutMonitor = new WorkerTimeoutMonitor(this.job_id, this.timeout,
				this.workerListener);
		Thread monitor = new Thread(workerTimeoutMonitor);
		// monitor.setDaemon(true);
		monitor.start();

		try {
			System.out.println(Thread.currentThread().getName() + " go");
			int i = 0;
			boolean flag = true;
			while (flag) {
				Thread.sleep(1000);
				System.out.println(i);

				i++;
			}

			this.workerListener.doUpdateStatus(this.job_id, WorkerStatus.End, "OK");
			System.out.println(Thread.currentThread().getName() + "stop");

		} finally {
			monitor.interrupt();
		}

		return this.job_id;
	}

}
