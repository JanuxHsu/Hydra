package hydra_framework.viper.core;

public abstract class ViperClient {

	ViperController viperController;

	public ViperClient(ViperController viperController) {

		this.viperController = viperController;
		this.viperController.setViperClient(this);
		this.viperController.setGuiTitle("Viper");
		this.viperController.showGui();

	}

	public abstract boolean open() throws Exception;

	public abstract void close();

	public abstract void sendMessage(String messageText);

	public abstract void registerClient();

}
