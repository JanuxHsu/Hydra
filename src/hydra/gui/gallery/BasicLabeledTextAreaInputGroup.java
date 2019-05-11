package hydra.gui.gallery;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hydra.gui.utils.GridBagLayoutHelper;

public class BasicLabeledTextAreaInputGroup extends JPanel {

	private static final long serialVersionUID = 9148707768191951356L;

	JLabel itemLabel;
	JTextField itemInput;
	JButton itemActionBtn;

	Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

	public BasicLabeledTextAreaInputGroup(Map<String, JTextArea> inputs) {

		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);

		// this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		int rowCount = 0;

		for (String labelName : inputs.keySet()) {

			JLabel label = new JLabel(labelName);
			label.setHorizontalAlignment(JLabel.TRAILING);
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

			JTextArea textArea = (JTextArea) inputs.get(labelName);
			textArea.setFont(defaultFont);
			textArea.setLineWrap(true);

			JScrollPane scrollPane = new JScrollPane(textArea);

			this.add(label);
			this.add(scrollPane);

			GridBagLayoutHelper.makeConstraints(gridBagLayout, label, 1, 1, 0, rowCount, 1.0, 1.0, 1);
			GridBagLayoutHelper.makeConstraints(gridBagLayout, scrollPane, 5, 1, 2, rowCount, 11.0, 1.0, 1);
			rowCount++;
		}
	}

}
