package hydra_framework.hydra.listeners;

public interface WorkerListener {

	public enum WorkerStatus {
		Start, End, Error, Stop, Timeout

	};

	public void doUpdateStatus(String job_id, WorkerStatus workerStatus, String statusText);
}
