package hydra.hydra.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;

import hydra.hydra.core.WorkerTimeoutMonitor;
import hydra.hydra.listeners.WorkerListener;
import hydra.hydra.listeners.WorkerProcessListener;
import hydra.hydra.listeners.WorkerListener.WorkerStatus;

public class WorkerCallable implements Callable<String> {
	private final String job_id;

	private final File workingDirectory;
	private final List<String> commands;
	private int timeout = 300;
	private WorkerListener workerListener;

	public WorkerCallable(JsonObject commandObj) {
		this.job_id = commandObj.get("job_id").getAsString();
		commands = Arrays.asList(commandObj.get("command").getAsString().split(" "));

		File workingDir = new File(commandObj.get("working_directory").getAsString());

		this.workingDirectory = workingDir.exists() && workingDir.isDirectory() ? workingDir : null;
		this.timeout = commandObj.get("timeout").getAsInt();
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

		System.out.println(this.executeCommand());

		this.workerListener.doUpdateStatus(this.job_id, WorkerStatus.End, "");

		return this.job_id;
	}

	private String executeCommand() {

		String stdout = null;
		try {

			ProcessBuilder processBuilder = new ProcessBuilder(this.commands);

			processBuilder.redirectErrorStream(true);

			if (this.workingDirectory != null) {
				processBuilder.directory(this.workingDirectory);
			}

			// monitor.setDaemon(true);

			Process process = processBuilder.start();

			WorkerTimeoutMonitor workerTimeoutMonitor = new WorkerTimeoutMonitor(this.job_id, this.timeout,
					this.workerListener, new WorkerProcessListener() {

						@Override
						public void killprocess() {
							process.destroy();

						}
					});
			Thread monitor = new Thread(workerTimeoutMonitor);
			monitor.start();

			stdout = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
			monitor.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stdout = e.getMessage();
		}

		return stdout;
	}

}
