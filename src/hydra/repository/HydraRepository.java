package hydra.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import hydra.hydra.model.HydraStatus;

public class HydraRepository {

	final HydraStatus hydraStatus;

	final Map<String, String> systeminfoMap = new LinkedHashMap<>();

	public Long last_recv_bytes = new Long(0);
	public Long last_sent_bytes = new Long(0);

	public HydraRepository() {
		hydraStatus = new HydraStatus();
	}

	public HydraStatus getHydraStatus() {
		return this.hydraStatus;
	}

	public Map<String, String> getSystemInfoMap() {
		return this.systeminfoMap;
	}

}
