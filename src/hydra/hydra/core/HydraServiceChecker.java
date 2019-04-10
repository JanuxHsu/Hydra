package hydra.hydra.core;

import java.util.Calendar;
import java.util.Date;

public class HydraServiceChecker implements Runnable {

	final HydraController hydraController;

	public HydraServiceChecker(HydraController hydraController) {
		this.hydraController = hydraController;
	}

	@Override
	public void run() {

		try {

			if (!hydraController.hydraRepository.getHydraStatus().isConnectedToServer()) {
				System.out.println("Checking...trying to connect to zola server!");
				hydraController.connectToTarget();
				// hydraController.registerClient();

			} else {

				Date lastAckTime = hydraController.hydraRepository.getHydraStatus().getLastAckTime();
				Date currentTime = Calendar.getInstance().getTime();
				if (lastAckTime == null || (currentTime.getTime() - lastAckTime.getTime()) > 15000) {
					// hydraController.hydraRepository.getHydraStatus().setConnectedToServer(false);

					if (hydraController.hydraRepository.getHydraStatus().isConnectedToServer()) {
						hydraController.disconnectToTarget();
					}

				} else {
					System.out.println("Checking...completed!");
					hydraController.sendHeartBeat();

					// this.hydraController.hydraRepository.getHydraStatus().setConnectedToServer(false);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
