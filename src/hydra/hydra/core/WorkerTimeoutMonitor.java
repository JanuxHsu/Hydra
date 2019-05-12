package hydra.hydra.core;

import java.util.Calendar;
import java.util.Date;

import hydra.hydra.listeners.WorkerListener;
import hydra.hydra.listeners.WorkerListener.WorkerStatus;
import hydra.hydra.listeners.WorkerProcessListener;

public class WorkerTimeoutMonitor implements Runnable {
	private final String job_id;
	WorkerListener workerListener;
	WorkerProcessListener workerProcessListener;
	int timeout = 300;
	Date startTime = Calendar.getInstance().getTime();

	public WorkerTimeoutMonitor(String job_id, int timeout, WorkerListener workerListener,
			WorkerProcessListener workerProcessListener) {
		this.job_id = job_id;
		this.timeout = timeout;
		this.workerListener = workerListener;
		this.workerProcessListener = workerProcessListener;
	}

	@Override
	public void run() {

		while (!Thread.interrupted()) {
			long lapsed = Calendar.getInstance().getTime().getTime() - startTime.getTime();

			if ((lapsed / 1000) > timeout) {
				break;
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				break;
			}
		}
		this.workerProcessListener.killprocess();
		this.workerListener.doUpdateStatus(this.job_id, WorkerStatus.Timeout,
				"Process completed or max runtime reached.");

	}

}
