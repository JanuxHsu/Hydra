package hydra.hydra.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import hydra.hydra.core.HydraController;

public class HydraClientSwingGui extends HydraClientGui {

	protected JFrame mainWindow;

	JLabel serverIndicator;
	JLabel serverStatusInfo;

	JLabel workerIndicator;

	JTextArea logArea;
	JProgressBar memoryBar;

	public HydraClientSwingGui(HydraController hydraController) {
		super(hydraController);
		JFrame window = new JFrame();

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

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

		JPanel dashboardPanel = new JPanel(new GridLayout(1, 3));

		dashboardPanel.setOpaque(true);
		dashboardPanel.setBackground(new Color(44, 62, 80));

		JPanel leftDashPanel = new JPanel(new GridLayout(2, 1));
		JLabel connectionIndicator = new JLabel("Initializing...");
		connectionIndicator.setOpaque(true);
		connectionIndicator.setHorizontalAlignment(JLabel.CENTER);
		connectionIndicator.setBackground(Color.GRAY);
		connectionIndicator.setForeground(Color.white);

		this.serverIndicator = connectionIndicator;
		leftDashPanel.add(connectionIndicator);

		JLabel workerIndicator = new JLabel("Initializing...");
		workerIndicator.setOpaque(true);
		workerIndicator.setHorizontalAlignment(JLabel.CENTER);
		workerIndicator.setBackground(Color.GRAY);
		workerIndicator.setForeground(Color.white);
		this.workerIndicator = workerIndicator;
		leftDashPanel.add(workerIndicator);

		JLabel hydraIcon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/hydra64.png"))));

		hydraIcon.setHorizontalAlignment(JLabel.CENTER);

		dashboardPanel.add(leftDashPanel);
		dashboardPanel.add(hydraIcon);

		JPanel rightDashPanel = new JPanel(new BorderLayout());
		JProgressBar memoryBar = new JProgressBar();
		memoryBar.setStringPainted(true);
		// memoryBar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		// memoryBar.setMaximum(100);
		this.memoryBar = memoryBar;

		rightDashPanel.add(memoryBar, BorderLayout.CENTER);
		dashboardPanel.add(rightDashPanel);

		dashboardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

//		for (int i = 1; i <= 4; i++) {
//			JLabel mainLabel = new JLabel("Main");
//			mainLabel.setHorizontalAlignment(JLabel.CENTER);
//			mainLabel.setOpaque(true);
//			mainLabel.setBackground(Color.gray);
//			// mainLabel.setHorizontalTextPosition(JLabel.CENTER);
//			dashboardPanel.add(mainLabel);
//		}

		// dashboardPanel.add(leftDashPanel, BorderLayout.WEST);
		topPanel.add(hostLabel, BorderLayout.NORTH);
		topPanel.add(dashboardPanel, BorderLayout.SOUTH);

		// topPanel.setPreferredSize(new Dimension(800, 80));

		return topPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(true);
		centerPanel.setBackground(new Color(44, 62, 80));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JTextArea logArea = new JTextArea();
		this.logArea = logArea;
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
		this.mainWindow.setTitle(title);

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

	@Override
	public void updateConnectionStatus(String status) {

		// status = status != null ? status.trim() : "-";
		displaySystemLog(status);

	}

	@Override
	public void updateIsServerConnected(boolean isConnected) {

		if (isConnected) {
			this.serverIndicator.setText("Server Status : Connected");
			this.serverIndicator.setBackground(new Color(39, 174, 96));

		} else {
			this.serverIndicator.setText("Server Status : Offline");
			this.serverIndicator.setBackground(Color.RED);
		}

	}

	@Override
	public void displaySystemLog(String line) {
		if (this.logArea.getLineCount() > 1000) {
			logArea.setText("");
		}

		line = String.format("%s%n", line);
		this.logArea.append(line);
	}

	@Override
	public void updateIsWorkerActive(boolean isWorking) {
		if (isWorking) {
			this.workerIndicator.setText("Worker Status : Running");
			this.workerIndicator.setBackground(new Color(39, 174, 96));

		} else {
			this.workerIndicator.setText("Worker Status : Idle");
			this.workerIndicator.setBackground(new Color(243, 156, 18));
		}

	}

	@Override
	public void updateMemoryUsages(Long freeMem, Long totalMem) {
		totalMem = totalMem > 0 ? totalMem : freeMem;

		Integer freePercent = ((Double) ((totalMem.doubleValue() - freeMem.doubleValue()) / totalMem.doubleValue() * 100))
				.intValue();
		this.memoryBar.setValue(freePercent);
		this.memoryBar.setString("Memory Usage : " + freePercent + "%");
	}

}
