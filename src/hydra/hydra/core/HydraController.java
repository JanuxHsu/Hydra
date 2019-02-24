package hydra.hydra.core;

import java.io.File;
import java.io.FileFilter;

import hydra.hydra.gui.HydraClientGui;
import hydra.hydra.gui.HydraClientSwingGui;
import hydra.viper.gui.ViperClientGui;
import hydra.viper.gui.ViperClientGui.connectionBtnState;
import hydra.viper.gui.ViperClientSwingGui;

public class HydraController {

	protected HydraClient clientCore;
	protected HydraClientGui clientGui;

	public HydraController(HydraConfig hydraConfig) {
		this.clientGui = new HydraClientSwingGui(this);
		this.setGuiTitle(hydraConfig.app_name);

	}

	public void sendCommand(String text) {
		System.out.println("Controller :" + text);
		this.clientCore.sendMessage(text);
		this.resetCommandInputState();

	}

	public void setGuiTitle(String title) {
		this.clientGui.setTitle(title);

	}

	public void showGui() {
		this.clientGui.show();

	}

	public void setHydraClient(HydraClient hydraClient) {
		this.clientCore = hydraClient;

	}

	public void setClientGui(HydraClientGui hydraClientGui) {
		this.clientGui = hydraClientGui;

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
