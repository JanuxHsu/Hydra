package hydra.zola.core;

public class ZolaHelper {

	private static ZolaHelper instance = null;

	ZolaController zolaController;

	private ZolaHelper() {
		// TODO Auto-generated constructor stub
	}

	public static ZolaHelper getInstance() {
		if (instance == null) {
			synchronized (ZolaHelper.class) {
				if (instance == null) {
					instance = new ZolaHelper();
				}
			}
		}
		return instance;
	}

	public void setZolaController(ZolaController controller) {
		this.zolaController = controller;
	}

	public ZolaController getController() {
		return this.zolaController;
	}
	

}
