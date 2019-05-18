package hydra_framework.viper.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandAgent implements Runnable {

	ViperGuiController controller;
	String[] commands;

	File workingDir;

	public CommandAgent(ViperGuiController controller, String[] commands, File workingDir) {
		this.controller = controller;
		this.commands = commands;
		this.workingDir = workingDir;
	}

	@Override
	public void run() {

		ProcessBuilder builder = new ProcessBuilder(this.commands);
		builder.directory(this.workingDir);
		builder.redirectErrorStream(true);
		Process process;

		this.controller.gui.resetInputState();
		BufferedReader reader = null;
		try {
			process = builder.start();

			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

			String zString = null;
			while ((zString = reader.readLine()) != null) {

				this.controller.gui.displayMessage(zString);
				// System.out.println(zString);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.controller.gui.displayMessage(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
