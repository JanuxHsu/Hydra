package hydra.hydra.core;

public abstract class HydraClient {

	HydraController hydraController;

	public HydraClient(HydraController hydraController) {
		this.hydraController = hydraController;
		this.hydraController.setHydraClient(this);
		this.hydraController.setGuiTitle("Viper");
		this.hydraController.showGui();
	}

	public abstract boolean open();

	public abstract void close();

	public abstract void broadcast(String message);

	public abstract void sendMessage(String text);

}
