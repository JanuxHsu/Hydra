package hydra.hydra.core;

public class HydraConfig {

	public static final String Swing = "swing";
	public String app_name;
	private String GUI_type = "swing";

	public void setGUI_type(String gui_type) {
		this.GUI_type = gui_type;

	}

	public String getGuiType() {
		return this.GUI_type;
	}

}
