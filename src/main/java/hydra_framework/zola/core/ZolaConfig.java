package hydra_framework.zola.core;

public class ZolaConfig {
	public enum GUI_Type {
		Swing
	}

	public final static String zolaVersion = "v1.20";
	public static String hydraClientVersion = zolaVersion;

	public String app_name;
	private GUI_Type GUI_type = GUI_Type.Swing;

	public Integer servicePort = 5978;
	public Integer httpServicePort = 8080;

	public GUI_Type getGuiType() {
		return this.GUI_type;
	}

	public void setGUI_type(GUI_Type swing) {
		this.GUI_type = swing;

	}
}
