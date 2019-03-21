package hydra.hydra.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

public class HydraClientSwingGui extends HydraClientGui {

	protected JFrame mainWindow;

	Font defaultFont = new Font(Font.MONOSPACED, Font.BOLD, 12);

	JLabel serverIndicator;
	JLabel serverStatusInfo;

	JLabel workerIndicator;

	JTextArea logArea;
	JProgressBar cpuBar;
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

		window.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/newHydra64.png")));

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(600, 200));
		window.setMinimumSize(new Dimension(600, 200));

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
		hostLabel.setFont(defaultFont);

		hostLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		JPanel dashboardPanel = new JPanel(new GridLayout(1, 3));

		dashboardPanel.setOpaque(true);
		dashboardPanel.setBackground(new Color(44, 62, 80));

		JPanel leftDashPanel = new JPanel(new GridLayout(2, 1, 2, 2));

		leftDashPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JLabel connectionIndicator = new JLabel("Initializing...");
		connectionIndicator.setOpaque(true);
		connectionIndicator.setHorizontalAlignment(JLabel.CENTER);
		connectionIndicator.setBackground(Color.GRAY);
		connectionIndicator.setForeground(Color.white);
		// connectionIndicator.setFont(defaultFont);
		connectionIndicator.setHorizontalAlignment(JLabel.LEFT);
		connectionIndicator.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		this.serverIndicator = connectionIndicator;
		leftDashPanel.add(connectionIndicator);

		JLabel workerIndicator = new JLabel("Initializing...");
		workerIndicator.setOpaque(true);
		workerIndicator.setHorizontalAlignment(JLabel.CENTER);
		workerIndicator.setBackground(Color.GRAY);
		workerIndicator.setForeground(Color.white);
		// workerIndicator.setFont(defaultFont);
		workerIndicator.setHorizontalAlignment(JLabel.LEFT);
		workerIndicator.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		this.workerIndicator = workerIndicator;
		leftDashPanel.add(workerIndicator);

		dashboardPanel.add(leftDashPanel);

		JLabel hydraIcon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/newHydra64.png"))));

		hydraIcon.setPreferredSize(new Dimension(60, 60));
		hydraIcon.setHorizontalAlignment(JLabel.CENTER);
		// hydraIcon.setPreferredSize(new Dimension(60, 60));

		dashboardPanel.add(hydraIcon);

		JPanel rightDashPanel = new JPanel(new BorderLayout());
		JProgressBar cpuBar = new JProgressBar();

		JProgressBar memoryBar = new JProgressBar();

		JPanel barContainer = new JPanel(new GridLayout(2, 1, 2, 2));

		barContainer.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		memoryBar.setStringPainted(true);

		cpuBar.setStringPainted(true);

		memoryBar.setFont(defaultFont);
		cpuBar.setFont(defaultFont);

		memoryBar.setBorder(BorderFactory.createLineBorder(Color.black));
		cpuBar.setBorder(BorderFactory.createLineBorder(Color.black));
		// memoryBar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		// memoryBar.setMaximum(100);
		this.memoryBar = memoryBar;
		this.cpuBar = cpuBar;

		barContainer.add(cpuBar);
		barContainer.add(memoryBar);

		rightDashPanel.add(barContainer, BorderLayout.CENTER);
		dashboardPanel.add(rightDashPanel);

		// dashboardPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

		dashboardPanel.setBorder(BorderFactory.createEmptyBorder(5, 35, 5, 35));

		// dashboardPanel.setPreferredSize(new Dimension(1000, 70));
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
		// displaySystemLog(status);

	}

	@Override
	public void updateIsServerConnected(boolean isConnected) {

		if (isConnected) {
			this.serverIndicator.setText("Server Status : Connected");
			this.serverIndicator.setBackground(new Color(39, 174, 96));

		} else {
			this.serverIndicator.setText("Server Status : Down");
			this.serverIndicator.setBackground(Color.RED);
		}

	}

	@Override
	public void displaySystemLog(String line) {
		if (this.logArea.getLineCount() > 6) {
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

		Integer freePercent = ((Double) ((totalMem.doubleValue() - freeMem.doubleValue()) / totalMem.doubleValue()
				* 100)).intValue();
		this.memoryBar.setValue(freePercent);
		this.memoryBar.setString("Memory Usage : " + freePercent + "%");

	}

	@Override
	public void updateSystemInfo(SystemInfo systemInfo) {

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		Double cpuUsage = processor.getSystemCpuLoad();
		String cpuUsageVal = String.format("%.2f", cpuUsage * 100);

		Integer cpuUsageText = ((Double) Double.parseDouble(cpuUsageVal)).intValue();

		this.cpuBar.setValue(cpuUsageText);
		this.cpuBar.setString("CPU Usage : " + cpuUsageVal + "%");

		GlobalMemory memory = systemInfo.getHardware().getMemory();

		Long availableMem = memory.getAvailable();
		Long totalMem = memory.getTotal();

		String usage = String.format("%.2f", 100 - (availableMem.doubleValue() / totalMem.doubleValue() * 100));

		Integer usageValue = ((Double) Double.parseDouble(usage)).intValue();

		this.memoryBar.setValue(usageValue);
		this.memoryBar.setString("Mem Usage : " + usage + "%");

	}

}
