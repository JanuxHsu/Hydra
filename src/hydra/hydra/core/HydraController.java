package hydra.hydra.core;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hydra.hydra.gui.HydraClientGui;
import hydra.hydra.gui.HydraClientSwingGui;
import hydra.model.HydraMessage;
import hydra.model.HydraMessage.MessageType;
import hydra.viper.gui.ViperClientGui.connectionBtnState;

public class HydraController {

	ExecutorService executorService = Executors.newCachedThreadPool();
	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	protected HydraClient clientCore;
	protected HydraClientGui clientGui;
	public volatile boolean isConnectedToServer = false;

	public HydraController(HydraConfig hydraConfig) {
		this.clientGui = new HydraClientSwingGui(this);
		this.setGuiTitle(hydraConfig.app_name);
		scheduledExecutorService.scheduleAtFixedRate(new HydraServiceChecker(this), 1, 10, TimeUnit.SECONDS);

	}

	public ExecutorService getExecutorPool() {
		return this.executorService;
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
			this.isConnectedToServer = true;
			this.setConnectionBtnState(connectionBtnState.Disconnect);
			this.systemLog("Zola Server Connected!");

		} else {
			this.isConnectedToServer = false;
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

	public void connectionClose() {
		this.isConnectedToServer = false;

	}

	public boolean registerClient() {

		HydraMessage hydraMessage = new HydraMessage("I'm Hydra", null, MessageType.REGISTER);
		System.out.println(hydraMessage.toString());
		this.sendCommand(hydraMessage.toString());
		return isConnectedToServer;
	}

	public void sendHeartBeat() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeStamp = sdf.format(Calendar.getInstance().getTime());
		HydraMessage hydraMessage = new HydraMessage(timeStamp, null, MessageType.HEARTBEAT);
		System.out.println(hydraMessage.toString());
		this.sendCommand(hydraMessage.toString());

	}

}
