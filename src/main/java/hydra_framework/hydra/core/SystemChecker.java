package hydra_framework.hydra.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import hydra_framework.model.HydraMessage;
import hydra_framework.model.HydraMessage.MessageType;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public class SystemChecker implements Runnable {

	HydraController hydraController;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	final SystemInfo systemInfo;

	final CentralProcessor centralProcessor;
	final OperatingSystem operatingSystem;
	final ComputerSystem computerSystem;

	Map<String, String> systemInfoMap;

	public SystemChecker(HydraController hydraController) {
		this.hydraController = hydraController;
		this.systemInfo = hydraController.getSystemInfo();
		this.centralProcessor = this.systemInfo.getHardware().getProcessor();
		this.operatingSystem = this.systemInfo.getOperatingSystem();
		this.computerSystem = this.systemInfo.getHardware().getComputerSystem();
		this.systemInfoMap = hydraController.hydraRepository.getSystemInfoMap();

	}

	@Override
	public void run() {

		try {
			this.hydraController.initTable();

			JsonObject fullInfoJson = new JsonObject();

			fullInfoJson.add("host", this.getHostInfo());
			fullInfoJson.add("disks", this.getDisksInfo());
			fullInfoJson.add("network", this.getNetwork());

			HydraMessage hydraMessage = new HydraMessage(fullInfoJson, null, MessageType.FULLSYSINFO);
			this.hydraController.sendCommand(hydraMessage.toString());
			// System.out.println(gson.toJson(fullInfoJson));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public JsonObject getHostInfo() {

		JsonObject hostInfo = new JsonObject();
		String hostName = "unknown";
		String hostIP = "unknown";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
			hostIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		long sysetmStartTime = Calendar.getInstance().getTimeInMillis() - processor.getSystemUptime() * 1000;

		Date bootTime = new Date(sysetmStartTime);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// System.out.println(simpleDateFormat.format(d));

		systemInfoMap.put("BootTime", simpleDateFormat.format(bootTime) + " ("
				+ FormatUtil.formatElapsedSecs(processor.getSystemUptime()) + ")");

		hostInfo.addProperty("hostName", hostName);
		hostInfo.addProperty("IP", hostIP);
		hostInfo.addProperty("vendor", computerSystem.getManufacturer());
		hostInfo.addProperty("serialNumber", computerSystem.getSerialNumber());
		hostInfo.addProperty("model", computerSystem.getModel());
		hostInfo.addProperty("os", operatingSystem.toString());
		hostInfo.addProperty("bootTime", simpleDateFormat.format(bootTime));
		hostInfo.addProperty("bootLapse", FormatUtil.formatElapsedSecs(processor.getSystemUptime()));
		return hostInfo;

	}

	public JsonArray getDisksInfo() {

		JsonArray jsonArray = new JsonArray();
		OSFileStore[] fileStores = operatingSystem.getFileSystem().getFileStores();

		for (OSFileStore fs : fileStores) {

			JsonObject jsonObject = new JsonObject();

			long usable = fs.getUsableSpace();
			long total = fs.getTotalSpace();

			jsonObject.addProperty("name", fs.getName());
			jsonObject.addProperty("usable", usable);
			jsonObject.addProperty("total", total);
			jsonObject.addProperty("free_percent", String.format("%.1f%%", 100d * usable / total));
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}

	public JsonObject getNetwork() {
		JsonObject jsonObject = new JsonObject();
		NetworkParams net = systemInfo.getOperatingSystem().getNetworkParams();
		JsonArray dnsList = new JsonArray();
		for (String server : net.getDnsServers()) {
			dnsList.add(server);
		}
		jsonObject.add("dns", dnsList);
		jsonObject.addProperty("gateway", net.getIpv4DefaultGateway());
		jsonObject.addProperty("domainName", net.getDomainName());

		return jsonObject;
	}

}
