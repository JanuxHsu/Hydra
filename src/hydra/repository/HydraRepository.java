package hydra.repository;

import hydra.hydra.model.HydraStatus;

public class HydraRepository {

	final HydraStatus hydraStatus;

	public HydraRepository() {
		hydraStatus = new HydraStatus();
	}
	
	public HydraStatus getHydraStatus() {
		return this.hydraStatus;
	}

}
