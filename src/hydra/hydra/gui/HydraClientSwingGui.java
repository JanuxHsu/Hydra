package hydra.hydra.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import hydra.hydra.core.HydraController;

public class HydraClientSwingGui extends HydraClientGui {

	protected JFrame mainWindow;

	public HydraClientSwingGui(HydraController hydraController) {
		super(hydraController);
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
		window.setPreferredSize(new Dimension(600, 400));

		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(setupTopPanel(), BorderLayout.NORTH);
		mainPanel.add(setupCenterPanel(), BorderLayout.CENTER);

		window.add(mainPanel);

		window.pack();
		window.setLocationRelativeTo(null);
		this.mainWindow = window;

	}

	private JPanel setupTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		String hostname = "Unknown";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel hostLabel = new JLabel(hostname);
		hostLabel.setOpaque(true);
		hostLabel.setBackground(Color.RED);
		hostLabel.setForeground(Color.WHITE);

		JPanel dashboardPanel = new JPanel(new GridLayout(0, 4));

		for (int i = 1; i <= 4; i++) {
			JLabel mainLabel = new JLabel("Main");
			mainLabel.setHorizontalAlignment(JLabel.CENTER);
			mainLabel.setOpaque(true);
			mainLabel.setBackground(Color.gray);
			// mainLabel.setHorizontalTextPosition(JLabel.CENTER);
			dashboardPanel.add(mainLabel);
		}

		topPanel.add(hostLabel, BorderLayout.NORTH);
		topPanel.add(dashboardPanel, BorderLayout.SOUTH);

		return topPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());

		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JTextArea logArea = new JTextArea();

		centerPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

		return centerPanel;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void resetInputState() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAutoCompleteKeyword() {
		// TODO Auto-generated method stub
		return null;
	}

}
