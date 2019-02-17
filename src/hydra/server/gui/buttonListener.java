package hydra.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class buttonListener implements ActionListener {

	ZolaServerGui form;

	public buttonListener(ZolaServerGui form) {
		this.form = form;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//this.form.refreshClientPanel();

	}

}
