package hydra_framework.hydra.core;

public class HydraConfig {

	public enum HydraType {
		DEPLOYED, DEV
	}

	public enum HydraGUI_Type {
		Swing
	}

	public static int heartBeat_interval = 5;
	public static String version = "v1.20";
	public static HydraType mode = HydraType.DEV;

	public String app_name;
	private HydraGUI_Type GUI_type = HydraGUI_Type.Swing;
	public String zolaHost = "localhost";
	public String zolaPort = "5978";

	public String clientVersion;

	public void setGUI_type(HydraGUI_Type gui_type) {
		this.GUI_type = gui_type;

	}

	public HydraGUI_Type getGuiType() {
		return this.GUI_type;
	}

}
