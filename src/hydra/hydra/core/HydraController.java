package hydra.hydra.core;

import java.io.File;
import java.io.FileFilter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import hydra.hydra.core.HydraConfig.HydraType;
import hydra.hydra.gui.HydraClientGui;
import hydra.hydra.gui.HydraClientSwingGui;
import hydra.hydra.gui.HydraClientSwingGui.IconMessageMode;
import hydra.model.HydraMessage;
import hydra.model.HydraMessage.MessageType;
import hydra.repository.HydraRepository;
import hydra.zola.model.HydraConnectionClient.ClientType;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class HydraController {

	final HydraType hydraMode;

	public final String clientVersion;

	final String ZolaServerHost;
	final int ZolaServerPort;
	SystemInfo systemInfo = new SystemInfo();
	public final HydraRepository hydraRepository;

	ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);

	ExecutorService executorService = Executors.newCachedThreadPool();

	protected HydraClient clientCore;
	protected HydraClientGui clientGui;
	// public volatile boolean isConnectedToServer = false;
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonParser jsonParser = new JsonParser();

	public HydraController(HydraConfig hydraConfig) {
		this.clientVersion = hydraConfig.clientVersion;
		this.hydraMode = HydraConfig.mode;

		this.hydraRepository = new HydraRepository();
		this.clientGui = new HydraClientSwingGui(this);

		this.ZolaServerHost = hydraConfig.zolaHost;
		this.ZolaServerPort = Integer.parseInt(hydraConfig.zolaPort);

		this.setGuiTitle(hydraConfig.app_name);

		scheduledThreadPoolExecutor.scheduleAtFixedRate(new HydraServiceChecker(this), 1,
				HydraConfig.heartBeat_interval, TimeUnit.SECONDS);

		scheduledThreadPoolExecutor.scheduleAtFixedRate(new SystemChecker(this), 5, 1800, TimeUnit.SECONDS);
		this.initTable();
	}

	public ExecutorService getExecutorPool() {
		return this.executorService;
	}

	public void sendCommand(String text) {
		// System.out.println("Controller :" + text);

		// System.out.println(text);
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
		this.clientGui.displayIconMessage("Hydra", "Disonnected to Server!", java.awt.TrayIcon.MessageType.WARNING,
				IconMessageMode.ALWAYS);

		this.hydraRepository.getHydraStatus().setConnectedToServer(false);
		this.updateRecv(null);
		this.updateHydraStatus();

	}

	public boolean doRegisterClient(JsonObject messageBody) {
		this.clientGui.updateClientInfo(messageBody);

		this.hydraRepository.update_file_url = messageBody.get("file_url").getAsString();

		JsonObject registerJson = new JsonObject();
		registerJson.addProperty("clientVersion", this.clientVersion);
		registerJson.addProperty("clientType", ClientType.HYDRA.toString());

		HydraMessage hydraMessage = new HydraMessage(registerJson, null, MessageType.REGISTER);

		this.sendCommand(hydraMessage.toString());
		return true;
	}

	public void sendHeartBeat() {
		JsonObject res = new JsonObject();
		Map<String, String> sysInfoMap = this.hydraRepository.getSystemInfoMap();

		try {
			res.addProperty("host", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		res.addProperty("interval", HydraConfig.heartBeat_interval);

		GlobalMemory memory = systemInfo.getHardware().getMemory();
		CentralProcessor processor = systemInfo.getHardware().getProcessor();
		OperatingSystem os = systemInfo.getOperatingSystem();
		// HydraStatus hydraStatus = this.hydraRepository.getHydraStatus();

		Double cpuUsage = processor.getSystemCpuLoad();

		String cpuUsageVal = String.format("%.2f", cpuUsage * 100);
		res.addProperty("cpu", cpuUsageVal);

		Long availableMem = memory.getAvailable();
		Long totalMem = memory.getTotal();

		String usage = String.format("%.2f", 100 - (availableMem.doubleValue() / totalMem.doubleValue() * 100));
		res.addProperty("memory", usage);

		JsonObject processObj = new JsonObject();

		String processCnt = Integer.toString(os.getProcessCount());
		String threadCnt = Integer.toString(os.getThreadCount());
		sysInfoMap.put("Processes", String.format("%s (Threads: %s)", processCnt, threadCnt));
		processObj.addProperty("process", processCnt);
		processObj.addProperty("threads", threadCnt);

		res.add("os", processObj);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timeStamp = sdf.format(Calendar.getInstance().getTime());

		NetworkIF[] networkIFs = systemInfo.getHardware().getNetworkIFs();

		Long totalPacketRecv = new Long(0);
		Long totalBytesRecv = new Long(0);
		Long totalPacketSent = new Long(0);
		Long totalBytesSent = new Long(0);
		Long totalNetErr = new Long(0);

		int totalValidInterfaceCnt = 0;

		for (NetworkIF net : networkIFs) {

			NetworkInterface networkInterface = net.getNetworkInterface();
			boolean isLoopBackInterface = false;
			boolean isUp = false;

			try {
				isLoopBackInterface = networkInterface.isLoopback();
				isUp = net.getNetworkInterface().isUp();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!isLoopBackInterface && isUp) {

				boolean hasData = net.getBytesRecv() > 0 || net.getBytesSent() > 0 || net.getPacketsRecv() > 0
						|| net.getPacketsSent() > 0;

				if (hasData) {
					totalPacketRecv += net.getPacketsRecv();
					totalBytesRecv += net.getBytesRecv();
					totalPacketSent += net.getPacketsSent();
					totalBytesSent += net.getBytesSent();

					totalNetErr += net.getOutErrors() + net.getInErrors();

				}

			}
			totalValidInterfaceCnt++;

		}

		JsonObject networkInfo = new JsonObject();

		Long delta_recv_bytes = totalBytesRecv
				- (this.hydraRepository.last_recv_bytes == 0 ? totalBytesRecv : this.hydraRepository.last_recv_bytes);
		Long delta_sent_bytes = totalBytesSent
				- (this.hydraRepository.last_sent_bytes == 0 ? totalBytesSent : this.hydraRepository.last_sent_bytes);
		this.hydraRepository.last_recv_bytes = totalBytesRecv;
		this.hydraRepository.last_sent_bytes = totalBytesSent;

		networkInfo.addProperty("workingInterfaces", totalValidInterfaceCnt);
		networkInfo.addProperty("totalPacketRecv", totalPacketRecv);
		networkInfo.addProperty("totalBytesRecv", totalBytesRecv);
		networkInfo.addProperty("totalBytesRecvDelta", delta_recv_bytes);
		networkInfo.addProperty("totalPacketSent", totalPacketSent);
		networkInfo.addProperty("totalBytesSent", totalBytesSent);
		networkInfo.addProperty("totalBytesSentDelta", delta_sent_bytes);
		networkInfo.addProperty("totalNetworkErr", totalNetErr);

		sysInfoMap.put("Traffic(Recv)",
				String.format("%s/s (Total: %s)",
						FormatUtil.formatBytes(delta_recv_bytes / HydraConfig.heartBeat_interval),
						FormatUtil.formatBytes(totalBytesRecv)));
		sysInfoMap.put("Traffic(Sent)",
				String.format("%s/s (Total: %s)",
						FormatUtil.formatBytes(delta_sent_bytes / HydraConfig.heartBeat_interval),
						FormatUtil.formatBytes(totalBytesSent)));

		res.add("network", networkInfo);

		res.addProperty("timestamp", timeStamp);

		// System.out.println(gson.toJson(res));

		HydraMessage hydraMessage = new HydraMessage(res, null, MessageType.HEARTBEAT);

		this.hydraRepository.getHydraStatus().setConnectionInfo(timeStamp);

		this.sendCommand(hydraMessage.toString());
		try {

			updateHydraStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateHydraStatus() {

//		System.out.println(this.scheduledThreadPoolExecutor.getActiveCount() + "/"
//				+ this.scheduledThreadPoolExecutor.getCorePoolSize());
		this.clientGui.updateSystemInfo(systemInfo);
		// this.clientGui.updateMemoryUsages(availableMem, totalMem);
		this.clientGui.updateConnectionStatus(this.hydraRepository.getHydraStatus().getConnectionInfo());
		this.clientGui.updateIsServerConnected(this.hydraRepository.getHydraStatus().isConnectedToServer());
		this.clientGui.updateIsWorkerActive(this.hydraRepository.getHydraStatus().isWorkerActive());

		this.refreshTable();
	}

	public void updateRecv(String line) {

		if (line == null) {
			return;
		}

		try {

			JsonElement recv_message_json = jsonParser.parse(line);

			if (recv_message_json.isJsonObject()) {
				// System.out.println(gson.toJson(recv_message_json));
				HydraMessage hydraMessage = gson.fromJson(line, HydraMessage.class);

				MessageType messageType = hydraMessage.getMessageType();
				JsonObject messageBody = gson.fromJson(line, JsonObject.class).get("message").getAsJsonObject();
				switch (messageType) {
				case REGISTER:

					if (messageBody != null) {

						this.systemLog("Zola Server Connected!");
						this.clientGui.displayIconMessage("Hydra", "Connected to Server!",
								java.awt.TrayIcon.MessageType.INFO, IconMessageMode.ALWAYS);

						this.doRegisterClient(messageBody);
					}
					break;

				case MANAGEMENT:

					if (messageBody != null) {
						String action = messageBody.get("action").getAsString();
						System.out.println(gson.toJson(messageBody));
						switch (action.toLowerCase()) {
						case "shutdown":
							System.exit(0);
							break;

						case "reset":
							this.clientCore.close();
							break;

						default:
							break;
						}
					}
					break;

				default:
					break;
				}
			}

			// jsonObject.get(memberName)

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.hydraRepository.getHydraStatus().setServerLastResponse(line);

	}

	public SystemInfo getSystemInfo() {
		return this.systemInfo;
	}

	public void refreshTable() {
		List<Object[]> rowList = new ArrayList<>();

		Map<String, String> infoMap = this.hydraRepository.getSystemInfoMap();

		for (String key : infoMap.keySet()) {
			ArrayList<String> row = new ArrayList<>();
			row.add(key);
			row.add(infoMap.get(key));

			rowList.add(row.toArray());

		}

		this.clientGui.refreshTable(rowList);

	}

	public void initTable() {
		Map<String, String> systemInfoMap = this.hydraRepository.getSystemInfoMap();
		SystemInfo systemInfo = this.getSystemInfo();

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		long sysetmStartTime = Calendar.getInstance().getTimeInMillis() - processor.getSystemUptime() * 1000;

		Date bootTime = new Date(sysetmStartTime);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// System.out.println(simpleDateFormat.format(d));

		systemInfoMap.put("BootTime", simpleDateFormat.format(bootTime) + " ("
				+ FormatUtil.formatElapsedSecs(processor.getSystemUptime()) + ")");

		systemInfoMap.put("Traffic(Recv)", "...");
		systemInfoMap.put("Traffic(Sent)", "...");

//		systemInfoMap.put("Core", "Physical/Logical : " + Integer.toString(processor.getPhysicalProcessorCount()) + "/"
//				+ Integer.toString(processor.getLogicalProcessorCount()));
		systemInfoMap.put("Processes", "...");
		OSFileStore[] fileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();

		for (OSFileStore fs : fileStores) {
			long usable = fs.getUsableSpace();
			long total = fs.getTotalSpace();

			systemInfoMap.put(fs.getName(), String.format("%.1f%% free, %s of %s", 100d * usable / total,
					FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace())));

		}

		NetworkParams net = systemInfo.getOperatingSystem().getNetworkParams();

		systemInfoMap.put("DNS", "[" + String.join(", ", net.getDnsServers()) + "]");
		systemInfoMap.put("Gateway", net.getIpv4DefaultGateway());
		systemInfoMap.put("DomainName", net.getDomainName());
	}

}
