package hydra.viper.core;

public class ViperConfig {

	public static enum ViperGUIType {
		Swing
	}

	public final static String Swing = "swing";

	private ViperGUIType GUI_type = ViperGUIType.Swing;

	public String app_name = "Viper";

	public String zolaHost = "localhost";

	public String zolaPort = "5978";

	public String zolaAPIPort = "8080";

	public ViperGUIType getGuiType() {
		return this.GUI_type;
	}

	public void setGUI_type(ViperGUIType type) {
		this.GUI_type = type;
	}
}
