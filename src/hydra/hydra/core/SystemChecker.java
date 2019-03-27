package hydra.hydra.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

public class SystemChecker implements Runnable {

	HydraController hydraController;

	public SystemChecker(HydraController hydraController) {
		this.hydraController = hydraController;
	}

	@Override
	public void run() {
		Map<String, String> systemInfoMap = this.hydraController.hydraRepository.getSystemInfoMap();
		SystemInfo systemInfo = this.hydraController.getSystemInfo();

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		long sysetmStartTime = Calendar.getInstance().getTimeInMillis() - processor.getSystemUptime() * 1000;

		Date bootTime = new Date(sysetmStartTime);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// System.out.println(simpleDateFormat.format(d));

		systemInfoMap.put("BootTime", simpleDateFormat.format(bootTime) + "("
				+ FormatUtil.formatElapsedSecs(processor.getSystemUptime()) + ")");
		systemInfoMap.put("CPU", processor.toString());
		systemInfoMap.put("Core", "Physical/Logical : " + Integer.toString(processor.getPhysicalProcessorCount()) + "/"
				+ Integer.toString(processor.getLogicalProcessorCount()));
		systemInfoMap.put("Processes", Integer.toString(systemInfo.getOperatingSystem().getThreadCount())
				+ String.format(" (Threads: %s)", systemInfo.getOperatingSystem().getProcessCount()));

		OSFileStore[] fileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();

		for (OSFileStore fs : fileStores) {
			long usable = fs.getUsableSpace();
			long total = fs.getTotalSpace();

			systemInfoMap.put(fs.getName(), String.format("(%.1f%%) free, %s of %s", 100d * usable / total, FormatUtil.formatBytes(usable),
					FormatUtil.formatBytes(fs.getTotalSpace())));

		}

		NetworkParams net = systemInfo.getOperatingSystem().getNetworkParams();

		systemInfoMap.put("DNS", "[" + String.join(", ", net.getDnsServers()) + "]");
		systemInfoMap.put("Gateway", net.getIpv4DefaultGateway());
		systemInfoMap.put("DomainName", net.getDomainName());

		this.hydraController.refreshTable();

		// TODO Auto-generated method stub

	}

}
