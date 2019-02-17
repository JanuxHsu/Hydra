package hydra.viper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendCmdListener implements ActionListener {

	ViperGuiController controller;

	public SendCmdListener(ViperGuiController controller) {

		System.out.println(controller);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String text = e.getActionCommand();

		// System.out.println(text);
		controller.sendCommand(text);

	}

}
