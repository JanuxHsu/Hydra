package hydra.viper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hydra.viper.core.ViperController;

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

			try {
				if (this.viperController.connectToTarget()) {
					this.viperController.setConnectionBtnState(ViperClientGui.connectionBtnState.Disconnect);

				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;

		case Disconnect:
			this.viperController.disconnectToTarget();
			this.viperController.setConnectionBtnState(ViperClientGui.connectionBtnState.Connect);
			break;

		default:
			break;
		}

	}

}
