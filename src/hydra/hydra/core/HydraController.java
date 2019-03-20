package hydra.hydra.core;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hydra.hydra.gui.HydraClientGui;
import hydra.hydra.gui.HydraClientSwingGui;
import hydra.model.HydraMessage;
import hydra.model.HydraMessage.MessageType;
import hydra.repository.HydraRepository;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

public class HydraController {

	final String ZolaServerHost;
	final int ZolaServerPort;
	SystemInfo systemInfo = new SystemInfo();
	final HydraRepository hydraRepository;
	ExecutorService executorService = Executors.newCachedThreadPool();
	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	protected HydraClient clientCore;
	protected HydraClientGui clientGui;
	// public volatile boolean isConnectedToServer = false;

	public HydraController(HydraConfig hydraConfig) {

		this.hydraRepository = new HydraRepository();
		this.clientGui = new HydraClientSwingGui(this);

		this.ZolaServerHost = hydraConfig.zolaHost;
		this.ZolaServerPort = Integer.parseInt(hydraConfig.zolaPort);

		this.setGuiTitle(hydraConfig.app_name);
		scheduledExecutorService.scheduleAtFixedRate(new HydraServiceChecker(this), 1, 5, TimeUnit.SECONDS);

	}

	public ExecutorService getExecutorPool() {
		return this.executorService;
	}

	public void sendCommand(String text) {
		// System.out.println("Controller :" + text);
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

		this.systemLog("Trying to connect to Zola Server using :" + this.ZolaServerHost + ":" + this.ZolaServerPort);
		boolean isConnected = false;
		try {
			isConnected = this.clientCore.open();
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (isConnected) {
			this.hydraRepository.getHydraStatus().setConnectedToServer(true);

			this.systemLog("Zola Server Connected!");

		} else {
			this.hydraRepository.getHydraStatus().setConnectedToServer(false);
			this.systemLog("Zola Server not responding, please try again!");

		}

		this.updateHydraStatus();

	}

	public void disconnectToTarget() {
		this.clientCore.close();

		this.systemLog("Connection to Zola Server closed!");

		this.hydraRepository.getHydraStatus().setConnectionInfo("Connection to Zola Server closed!");
		this.updateHydraStatus();
	}

	public void systemLog(String line) {
		this.hydraRepository.getHydraStatus().setConnectionInfo(line);
		this.clientGui.displaySystemLog(line);

	}

	public void connectionClose() {
		this.hydraRepository.getHydraStatus().setConnectedToServer(false);
		this.updateHydraStatus();

	}

	public boolean registerClient() {

		HydraMessage hydraMessage = new HydraMessage("I'm Hydra", null, MessageType.REGISTER);

		this.sendCommand(hydraMessage.toString());
		return true;
	}

	public void sendHeartBeat() {
		JsonObject res = new JsonObject();

		GlobalMemory memory = systemInfo.getHardware().getMemory();
		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		Double cpuUsage = processor.getSystemCpuLoad();
		String cpuUsageVal = String.format("%.2f", cpuUsage * 100);
		res.addProperty("cpu", cpuUsageVal);

		Long availableMem = memory.getAvailable();
		Long totalMem = memory.getTotal();

		String usage = String.format("%.2f", 100 - (availableMem.doubleValue() / totalMem.doubleValue() * 100));
		res.addProperty("memory", usage);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeStamp = sdf.format(Calendar.getInstance().getTime());

		res.addProperty("timestamp", timeStamp);

		HydraMessage hydraMessage = new HydraMessage(new Gson().toJson(res), null, MessageType.HEARTBEAT);

		this.hydraRepository.getHydraStatus().setConnectionInfo(timeStamp);

		this.sendCommand(hydraMessage.toString());
		try {

			updateHydraStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateHydraStatus() {

		this.clientGui.updateSystemInfo(systemInfo);
		// this.clientGui.updateMemoryUsages(availableMem, totalMem);
		this.clientGui.updateConnectionStatus(this.hydraRepository.getHydraStatus().getConnectionInfo());
		this.clientGui.updateIsServerConnected(this.hydraRepository.getHydraStatus().isConnectedToServer());
		this.clientGui.updateIsWorkerActive(this.hydraRepository.getHydraStatus().isWorkerActive());
	}

}
