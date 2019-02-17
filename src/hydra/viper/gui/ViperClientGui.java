package hydra.viper.gui;

public abstract class ViperClientGui {

	public ViperClientGui() {

	}

	public abstract void setTitle(String title);

	public abstract void show();

	public abstract void hide();

	public abstract void close();

	public abstract void displayMessage(String messages);

}
