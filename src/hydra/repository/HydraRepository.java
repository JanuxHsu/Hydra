package hydra.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import hydra.hydra.model.HydraStatus;

public class HydraRepository {

	final HydraStatus hydraStatus;

	final Map<String, String> systeminfo = new LinkedHashMap<>();

	public HydraRepository() {
		hydraStatus = new HydraStatus();
	}

	public HydraStatus getHydraStatus() {
		return this.hydraStatus;
	}

	public Map<String, String> getSystemInfoMap() {
		return this.systeminfo;
	}

}
