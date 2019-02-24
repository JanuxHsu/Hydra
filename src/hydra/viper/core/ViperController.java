package hydra.viper.core;

import java.io.File;
import java.io.FileFilter;

import hydra.viper.gui.ViperClientGui;
import hydra.viper.gui.ViperClientGui.connectionBtnState;
import hydra.viper.gui.ViperClientSwingGui;

public class ViperController {

	protected ViperClient clientCore;
	protected ViperClientGui clientGui;

	public ViperController(ViperConfig viperConfig) {
		this.clientGui = new ViperClientSwingGui(this);
		this.setGuiTitle(viperConfig.app_name);

	}

	public void sendCommand(String text) {
		System.out.println("Controller :" + text);

	}

	public void setGuiTitle(String title) {
		this.clientGui.setTitle(title);

	}

	public void showGui() {
		this.clientGui.show();

	}

	public void setViperClient(ViperClient viperClient) {
		this.clientCore = viperClient;

	}

	public void setClientGui(ViperClientGui viperClientGui) {
		this.clientGui = viperClientGui;

	}

	public void resetCommandInputState() {
		this.clientGui.resetInputState();
	}

	public String getAutoCompleteKeyword() {

		return this.clientGui.getAutoCompleteKeyword();

	}

	public File[] listCurrentPathFiles(FileFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void autoCompleteKeyWord(String newWord) {
		// TODO Auto-generated method stub

	}

	public void connectToTarget() {

		this.systemLog("Trying to connect to Zola Server...");
		boolean isConnected = false;
		try {
			isConnected = this.clientCore.open();
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (isConnected) {
			this.setConnectionBtnState(connectionBtnState.Disconnect);
			this.systemLog("Zola Server Connected!");

		} else {
			this.setConnectionBtnState(connectionBtnState.Connect);
			this.systemLog("Zola Server not responding, please try again!");
		}

	}

	public void disconnectToTarget() {
		this.clientCore.close();
		this.setConnectionBtnState(connectionBtnState.Connect);
		this.systemLog("Connection to Zola Server closed!");
	}

	public void setConnectionBtnState(connectionBtnState connectState) {
		this.clientGui.setConnectionBtnState(connectState);
	}

	public void systemLog(String line) {
		this.clientGui.displaySystemLog(line);

	}

}
