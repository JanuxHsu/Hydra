package hydra.viper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import hydra.viper.core.ViperController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class ViperClientSwingGui extends ViperClientGui {

	JTextArea debugMessageBox;

	JButton connectionBtn;
	JTextField commandInputBox;

	JLabel currDir;
	JTextArea remoteConsoleTextArea;

	WebView webView;

	public ViperClientSwingGui(ViperController viperController) {
		super(viperController);

		JFrame window = new JFrame();

		// this.controller = new ViperGuiController(this);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(1000, 600));

		window.add(getSidePanel(), BorderLayout.WEST);
		window.add(getMainPanel(), BorderLayout.CENTER);
		window.add(getDebugPanel(), BorderLayout.EAST);

		window.pack();
		window.setLocationRelativeTo(null);
		this.mainWindow = window;

		getDefaultPath();

	}

	private JPanel getSidePanel() {
		JPanel sidePanel = new JPanel(new BorderLayout());

		sidePanel.setOpaque(true);
		sidePanel.setBackground(new Color(24, 44, 97));

		sidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel connectionPanel = new JPanel(new BorderLayout());

		JComboBox<String> hydraClientsComboBox = new JComboBox<>();

		// hydraClientsComboBox.setPreferredSize(null);
		hydraClientsComboBox.addItem("localhost");
		hydraClientsComboBox.addItem("TTTT");

		hydraClientsComboBox.setPreferredSize(new Dimension(0, 25));

		connectionPanel.add(hydraClientsComboBox, BorderLayout.NORTH);

		JPanel connectionButtonPanel = new JPanel(new GridLayout(2, 0));
		connectionButtonPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		JButton connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ViperConnectionListener(this.viperController));
		connectBtn.setBackground(new Color(39, 174, 96));
		connectBtn.setForeground(Color.WHITE);
		this.connectionBtn = connectBtn;

		connectionButtonPanel.add(new JButton("Get Hydras"));
		connectionButtonPanel.add(connectBtn);

		connectionButtonPanel.setPreferredSize(new Dimension(150, 50));

		connectionPanel.add(connectionButtonPanel, BorderLayout.CENTER);

		sidePanel.add(connectionPanel, BorderLayout.NORTH);

		return sidePanel;
	}

	private JPanel getMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		// JPanel logPanel = new JPanel(new GridLayout(1, 1));
		JFXPanel webViewPanel = new JFXPanel();

		Platform.runLater(() -> {
			WebView webView = new WebView();
			webViewPanel.setScene(new Scene(webView));
			webView.getEngine().load("http://172.20.10.8:8099/api/clients");

		});
		//webViewPanel.setScene(new Scene(this.webView));
//		JTextArea logArea = new JTextArea();
//
//		logArea.setForeground(Color.white);
//		logArea.setOpaque(true);
//		logArea.setBackground(new Color(44, 58, 71));
//		logArea.setCaretColor(Color.white);
//		logArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
//		DefaultCaret caret = (DefaultCaret) logArea.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// this.remoteConsoleTextArea = webView;

		JPanel titlePanel = new JPanel(new GridLayout(1, 3));

		JLabel titlebar = new JLabel("Remote Output Console");
		titlebar.setOpaque(true);
		titlebar.setBackground(new Color(24, 44, 97));
		titlebar.setForeground(Color.WHITE);
		titlebar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		titlePanel.add(titlebar);

		JPanel inputPanel = new JPanel(new BorderLayout());

		File file = new File(System.getProperty("user.dir"));

		JLabel currentDir = new JLabel(file.getName() + " > ");

		currentDir.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		this.currDir = currentDir;
		JTextField inputBox = new JTextField();

		inputBox.setFocusTraversalKeysEnabled(false);
		// inputBox.addActionListener(new SendCmdListener(this.controller));

		System.out.println("init:" + this.viperController);

		inputBox.addActionListener(new SendCmdListener(this.viperController));
		inputBox.addKeyListener(new UserActionListener(this.viperController));

		this.commandInputBox = inputBox;

		inputPanel.add(currentDir, BorderLayout.WEST);
		inputPanel.add(inputBox, BorderLayout.CENTER);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(webViewPanel, BorderLayout.CENTER);
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

	@Override
	public void displayMessage(String message) {

		this.remoteConsoleTextArea.append(String.format("%s%n", message));
		this.remoteConsoleTextArea.update(this.remoteConsoleTextArea.getGraphics());

		// this.remoteConsoleTextArea.paintImmediately(this.remoteConsoleTextArea.getBounds());

	}

	public void changeDir(File newPath) {
		this.currDir.setText(newPath.getName() + " > ");
	}

	@Override
	public void resetInputState() {
		this.remoteConsoleTextArea.setText("");
		this.commandInputBox.setText("");
	}

	public void getDefaultPath() {
		//this.remoteConsoleTextArea.setText("");
		//this.remoteConsoleTextArea.append(System.getProperty("user.dir"));
	}

	@Override
	public String getAutoCompleteKeyword() {
		String[] words = this.commandInputBox.getText().split(" ");

		String keyword = words[words.length - 1];
		if (keyword.equals(this.commandInputBox.getText())) {
			return "";
		} else if (keyword.toLowerCase().equals("cd")) {
			return "";
		}

		return words[words.length - 1];
	}

	public void autoComplete(String autoCompleteText) {

		String[] origin = this.commandInputBox.getText().split(" ");

		origin[origin.length - 1] = autoCompleteText;

		this.commandInputBox.setText(String.join(" ", origin));
	}

	@Override
	public void setConnectionBtnState(connectionBtnState connectState) {
		this.connectionBtn.setText(connectState.toString());
		if (connectState == connectionBtnState.Disconnect) {
			this.connectionBtn.setBackground(Color.RED);
		} else {
			this.connectionBtn.setBackground(new Color(39, 174, 96));
		}
		this.connectionBtn.revalidate();
		this.connectionBtn.repaint();
	}

	@Override
	public void displaySystemLog(String line) {

		line = String.format("%s%n", line);
		this.debugMessageBox.append(line);

	}

}
