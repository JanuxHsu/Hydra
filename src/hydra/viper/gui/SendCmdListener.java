package hydra.viper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hydra.viper.core.ViperController;

public class SendCmdListener implements ActionListener {

	ViperGuiController clientController;

	ViperController viperController;

	public SendCmdListener(ViperGuiController controller) {

		// System.out.println(controller);
		this.clientController = controller;
	}

	public SendCmdListener(ViperController controller) {

		// System.out.println(controller);
		this.viperController = controller;
		// this.clientController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String text = e.getActionCommand();

		System.out.println(this.viperController);
		this.viperController.sendCommand(text);
		this.viperController.resetCommandInputState();
		// clientController.sendCommand(text);

	}

}
