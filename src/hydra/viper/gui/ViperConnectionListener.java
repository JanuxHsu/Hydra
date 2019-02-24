package hydra.viper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

import hydra.viper.core.ViperController;

public class ViperConnectionListener implements ActionListener {

	ViperController viperController;

	public ViperConnectionListener(ViperController viperController) {
		this.viperController = viperController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		ViperClientGui.connectionBtnState state = ViperClientGui.connectionBtnState.valueOf(e.getActionCommand());
		switch (state) {
		case Connect:
			this.viperController.connectToTarget();
			this.viperController.setConnectionBtnState(ViperClientGui.connectionBtnState.Disconnect);
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
