package hydra.hydra.core;

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

				System.out.println("Checking...completed!");
				hydraController.sendHeartBeat();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
