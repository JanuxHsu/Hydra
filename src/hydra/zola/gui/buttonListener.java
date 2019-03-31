package hydra.zola.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaHelper;

public class buttonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ZolaController zolaController = ZolaHelper.getInstance().getController();
		zolaController.shutDownAllClient();
	}

}
