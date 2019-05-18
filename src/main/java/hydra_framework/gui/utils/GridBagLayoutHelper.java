package hydra_framework.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;

public class GridBagLayoutHelper {
	public static void makeConstraints(GridBagLayout gbl, JComponent comp, int w, int h, int x, int y, double weightx,
			double weighty, int inset) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(inset, 0, inset, 0);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		gbl.setConstraints(comp, constraints);
	}
}
