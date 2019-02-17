package hydraServerGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class buttonListener implements ActionListener {

	HydraServerGui form;

	public buttonListener(HydraServerGui form) {
		this.form = form;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.form.refreshClientPanel();

	}

}
