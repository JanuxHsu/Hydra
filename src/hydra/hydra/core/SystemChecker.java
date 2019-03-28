package hydra.hydra.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.FormatUtil;

public class SystemChecker implements Runnable {

	HydraController hydraController;

	public SystemChecker(HydraController hydraController) {
		this.hydraController = hydraController;
	}

	@Override
	public void run() {
		this.hydraController.initTable();
		
		Map<String, String> systemInfoMap = this.hydraController.hydraRepository.getSystemInfoMap();

		SystemInfo systemInfo = this.hydraController.getSystemInfo();

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		long sysetmStartTime = Calendar.getInstance().getTimeInMillis() - processor.getSystemUptime() * 1000;

		Date bootTime = new Date(sysetmStartTime);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// System.out.println(simpleDateFormat.format(d));

		systemInfoMap.put("BootTime", simpleDateFormat.format(bootTime) + " ("
				+ FormatUtil.formatElapsedSecs(processor.getSystemUptime()) + ")");

	}

}
