package hydra.hydra.core;

public class HydraServiceChecker implements Runnable {

	final HydraController hydraController;

	public HydraServiceChecker(HydraController hydraController) {
		this.hydraController = hydraController;
	}

	@Override
	public void run() {

		System.out.println("Checking...");
		if (!hydraController.isConnectedToServer) {
			System.out.println("Checking...trying to connect to zola server!");
			hydraController.connectToTarget();
			hydraController.registerClient();

		} else {

			System.out.println("Checking...completed!");
			hydraController.sendHeartBeat();
		}
	}

}
