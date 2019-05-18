package hydra_framework.viper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hydra_framework.viper.core.ViperController;

public class ViperConnectionListener implements ActionListener {

	ViperController viperController;

	public ViperConnectionListener(ViperController viperController) {
		this.viperController = viperController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ViperClientGui.connectionBtnState state = ViperClientGui.connectionBtnState.valueOf(e.getActionCommand());
		switch (state) {
		case Connect:
			this.viperController.connectToTarget();

			break;

		case Disconnect:
			this.viperController.disconnectToTarget();

			break;

		default:
			break;
		}

	}

}
