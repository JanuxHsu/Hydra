package hydra.viper.core;

public class ViperConfig {
	public final static String Swing = "swing";

	private String GUI_type = "swing";

	public String app_name = "Viper";

	public String zolaHost = "localhost";

	public String zolaPort = "5978";

	public String getGuiType() {
		return this.GUI_type;
	}

	public void setGUI_type(String type) {
		this.GUI_type = type;
	}
}
