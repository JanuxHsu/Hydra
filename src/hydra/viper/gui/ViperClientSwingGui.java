package hydra.viper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ViperClientSwingGui extends ViperClientGui {

	protected JFrame mainWindow;

	protected ViperGuiController controller;

	JTextArea debugMessageBox;

	JTextField commandInputBox;

	public ViperClientSwingGui() {
		JFrame window = new JFrame();
		
		this.controller = new ViperGuiController(this);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(1100, 400));

		window.add(getSidePanel(), BorderLayout.WEST);
		window.add(getMainPanel(), BorderLayout.CENTER);
		window.add(getDebugPanel(), BorderLayout.EAST);

		window.pack();
		window.setLocationRelativeTo(null);
		this.mainWindow = window;

	}

	private JPanel getSidePanel() {
		JPanel sidePanel = new JPanel(new BorderLayout());

		sidePanel.setOpaque(true);
		sidePanel.setBackground(new Color(24, 44, 97));
		sidePanel.setPreferredSize(new Dimension(200, 0));
		sidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JComboBox<String> hydraClientsComboBox = new JComboBox<>();

		// hydraClientsComboBox.setPreferredSize(null);
		hydraClientsComboBox.addItem("localhost");
		hydraClientsComboBox.addItem("TTTT");

		hydraClientsComboBox.setPreferredSize(new Dimension(0, 25));

		sidePanel.add(hydraClientsComboBox, BorderLayout.NORTH);

		return sidePanel;
	}

	private JPanel getMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		// JPanel logPanel = new JPanel(new GridLayout(1, 1));
		JTextArea logArea = new JTextArea();

		logArea.setForeground(Color.white);
		logArea.setOpaque(true);
		logArea.setBackground(new Color(44, 58, 71));
		logArea.setCaretColor(Color.white);
		logArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		JPanel titlePanel = new JPanel(new GridLayout(1, 3));

		JLabel titlebar = new JLabel("Remote Output Console");
		titlebar.setOpaque(true);
		titlebar.setBackground(new Color(24, 44, 97));
		titlebar.setForeground(Color.WHITE);
		titlebar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		titlePanel.add(titlebar);

		JPanel inputPanel = new JPanel(new GridLayout(1, 1));
		JTextField inputBox = new JTextField();

		inputBox.addActionListener(new SendCmdListener(this.controller));
		this.commandInputBox = inputBox;
		inputPanel.add(inputBox);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
		mainPanel.add(inputPanel, BorderLayout.SOUTH);

		return mainPanel;
	}

	public JPanel getDebugPanel() {
		JPanel debugPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Message:");
		label.setOpaque(true);
		label.setBackground(new Color(24, 44, 97));
		label.setForeground(Color.WHITE);
		label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		// label.setPreferredSize(new Dimension(300, 30));

		JTextArea debugTextArea = new JTextArea();

		debugTextArea.setForeground(Color.white);
		debugTextArea.setOpaque(true);
		debugTextArea.setBackground(new Color(44, 58, 71));
		debugTextArea.setCaretColor(Color.white);
		debugTextArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		this.debugMessageBox = debugTextArea;

		debugPanel.add(label, BorderLayout.NORTH);
		debugPanel.add(new JScrollPane(debugTextArea), BorderLayout.CENTER);

		debugPanel.setPreferredSize(new Dimension(300, 0));
		return debugPanel;

	}

	@Override
	public void show() {
		this.mainWindow.setVisible(true);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitle(String title) {
		this.mainWindow.setTitle(title);

	}

	public void displayMessage(String message) {
		this.debugMessageBox.append(String.format("%s%n", message));
		this.commandInputBox.setText("");
	}

}
