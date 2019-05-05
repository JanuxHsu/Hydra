package hydra.gui.utils;

import javax.swing.table.DefaultTableModel;

public class ZolaTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -7429475852213361903L;

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
