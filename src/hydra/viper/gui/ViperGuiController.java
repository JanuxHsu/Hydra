package hydra.viper.gui;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ViperGuiController {

	protected ViperClientSwingGui gui;
	ExecutorService executorService = Executors.newCachedThreadPool();

	File currentWorkingDir = new File(System.getProperty("user.dir"));

	String autoKeyword;

	public ViperGuiController(ViperClientSwingGui gui) {
		this.gui = gui;
	}

	public void sendCommand(String command) {

		String[] commands = command.split(" ");

		if (commands[0].toLowerCase().equals("cd")) {

			if (commands[1].toLowerCase().equals("..")) {
				this.currentWorkingDir = this.currentWorkingDir.getParentFile();
				commands = new String[] { "pwd" };
				CommandAgent commandAgent = new CommandAgent(this, commands, currentWorkingDir);
				executorService.execute(commandAgent);
				this.gui.changeDir(this.currentWorkingDir);
			} else {

				for (File subFile : this.currentWorkingDir.listFiles()) {

					String autoWord = commands[1].toLowerCase();
					String subWord = subFile.getName().toLowerCase() + File.separatorChar;
					// System.out.println(autoWord + " || " + subWord + " || " +
					// subWord.equals(autoWord));

					if (autoWord.equals(subWord)) {
						this.currentWorkingDir = subFile;

						System.out.println(this.currentWorkingDir.getAbsolutePath());
						break;
					}

				}
				this.gui.changeDir(this.currentWorkingDir);

				commands = new String[] { "pwd" };
				CommandAgent commandAgent = new CommandAgent(this, commands, currentWorkingDir);
				executorService.execute(commandAgent);

			}

		} else if (commands[0].toLowerCase().equals("stop")) {
			System.out.println("adwdw");
			this.closeThreadPool();
		} else {
			CommandAgent commandAgent = new CommandAgent(this, commands, currentWorkingDir);
			executorService.execute(commandAgent);

		}
	}

	public void closeThreadPool() {
		System.out.println("zz");
		// this.executorService.shutdown();
		this.executorService.shutdownNow();
		try {
			this.executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(this.executorService.isTerminated());
	}

}
