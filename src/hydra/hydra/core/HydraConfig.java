package hydra.hydra.core;

public class HydraConfig {

	public enum GUI_Type {
		Swing
	}

	public String app_name;
	private GUI_Type GUI_type = GUI_Type.Swing;
	public String zolaHost = "localhost";
	public String zolaPort = "5978";

	public String clientVersion;

	public void setGUI_type(GUI_Type gui_type) {
		this.GUI_type = gui_type;

	}

	public GUI_Type getGuiType() {
		return this.GUI_type;
	}

}
