package hydra.hydra.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonObject;

import hydra.gui.utils.TableColumnAdjuster;
import hydra.hydra.core.HydraController;
import hydra.utils.HydraUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

public class HydraClientSwingGui extends HydraClientGui {

	public enum IconMessageMode {
		ALWAYS, PERIODIC
	}

	protected JFrame mainWindow;

	TrayIcon trayIcon;

	Font monoFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
	Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

	JLabel hostLabel;

	JLabel serverIndicator;
	JLabel serverStatusInfo;

	JLabel workerIndicator;

	JTable systemInfoTable;

	JTextArea logArea;
	JProgressBar cpuBar;
	JProgressBar memoryBar;

	private Long iconShowMessageTimestamp = Calendar.getInstance().getTimeInMillis();

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

		// window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(300, 400));
		window.setMinimumSize(new Dimension(300, 400));

		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(setupTopPanel(), BorderLayout.NORTH);
		mainPanel.add(setupCenterPanel(), BorderLayout.CENTER);

		window.add(mainPanel);

		window.pack();
		window.setLocationRelativeTo(null);
		this.mainWindow = window;

		this.trayIcon = this.addSystemTray();

		TrayIcon icon = this.trayIcon;

		this.mainWindow.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				icon.displayMessage("Hydra", "Hydra is still running!", MessageType.INFO);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private TrayIcon addSystemTray() {
		HydraController hydraController = this.hydraController;
		JFrame main = this.mainWindow;

		// checking for support
		if (!SystemTray.isSupported()) {
			System.out.println("System tray is not supported !!! ");
			return null;
		}
		// get the systemTray of the systems
		SystemTray systemTray = SystemTray.getSystemTray();

		Image image = Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/newHydra64.png"));

		// Pop-up menu
		PopupMenu trayPopupMenu = new PopupMenu();

		// add menu item for Pop-up menu
		MenuItem version = new MenuItem("Version : " + this.hydraController.clientVersion);
		version.setEnabled(false);
		trayPopupMenu.add(version);
		trayPopupMenu.addSeparator();

		MenuItem status = new MenuItem("Status: Unknown");
		status.setEnabled(false);
		trayPopupMenu.add(status);
		trayPopupMenu.addSeparator();

		MenuItem update = new MenuItem("Check Update");

		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String updateUrl = hydraController.hydraRepository.update_file_url;
				HydraUtils.getRunningJarName();
				boolean isDownloadOK = HydraUtils.downloadNewClient(updateUrl, "tmp_" + HydraUtils.getRunningJarName());

				if (isDownloadOK) {
					System.out.println("Download OK!");
					System.exit(1);
				} else {
					System.err.println("Download Fail.");
				}

			}

		});
		trayPopupMenu.add(update);

		MenuItem action = new MenuItem("Show");

		action.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				main.setVisible(true);
			}
		});
		trayPopupMenu.add(action);

		// 2nd menuitem of popupmenu
		MenuItem close = new MenuItem("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		trayPopupMenu.add(close);

		// setting tray icon
		TrayIcon trayIcon = new TrayIcon(image, this.mainWindow.getTitle(), trayPopupMenu);
		// adjust to default size as per system recommendation
		trayIcon.setImageAutoSize(true);

		try {
			systemTray.add(trayIcon);
		} catch (AWTException awtException) {
			awtException.printStackTrace();
		}
		// trayIcon.displayMessage("Hello, World", "notification demo",
		// MessageType.NONE);
		return trayIcon;
	}

	private JPanel setupTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());

		JPanel topBar = new JPanel(new BorderLayout());
		JLabel hostLabel = new JLabel("Connecting...");
		this.hostLabel = hostLabel;
		hostLabel.setOpaque(true);
		hostLabel.setBackground(Color.RED);
		hostLabel.setForeground(Color.WHITE);
		hostLabel.setFont(defaultFont);

		hostLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		JLabel hydraIcon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/newHydra24.png"))));
		hydraIcon.setHorizontalAlignment(JLabel.CENTER);
		topBar.add(hydraIcon, BorderLayout.WEST);
		topBar.add(hostLabel, BorderLayout.CENTER);

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

		connectionIndicator.setFont(defaultFont);

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
		workerIndicator.setFont(defaultFont);

		this.workerIndicator = workerIndicator;
		leftDashPanel.add(workerIndicator);

		dashboardPanel.add(leftDashPanel);

//		JLabel hydraIcon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
//				.getImage(this.getClass().getClassLoader().getResource("resources/newHydra32.png"))));
//
//		hydraIcon.setPreferredSize(new Dimension(60, 60));
//		hydraIcon.setHorizontalAlignment(JLabel.CENTER);
//		// hydraIcon.setPreferredSize(new Dimension(60, 60));
//
//		dashboardPanel.add(hydraIcon);

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

		dashboardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// dashboardPanel.setPreferredSize(new Dimension(1000, 70));
		topPanel.add(topBar, BorderLayout.NORTH);
		topPanel.add(dashboardPanel, BorderLayout.SOUTH);

		// topPanel.setPreferredSize(new Dimension(800, 80));

		return topPanel;
	}

	private JPanel setupCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());

		centerPanel.setOpaque(true);
		centerPanel.setBackground(new Color(44, 62, 80));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Item");
		tableModel.addColumn("Value");

		JTable resultTable = new JTable(tableModel);

		resultTable.setFont(defaultFont);
		resultTable.getTableHeader().setFont(defaultFont);

		resultTable.setAutoCreateRowSorter(true);

		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		this.systemInfoTable = resultTable;
		centerPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

		JTextArea logArea = new JTextArea();
		logArea.setLineWrap(true);
		logArea.setPreferredSize(new Dimension(0, 50));
		logArea.setEditable(false);
		logArea.setFont(defaultFont);
		this.logArea = logArea;
		centerPanel.add(new JScrollPane(logArea), BorderLayout.SOUTH);

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

		SwingUtilities.invokeLater(() -> {
			if (isConnected) {
				this.serverIndicator.setText("Server Status : Up");
				this.serverIndicator.setBackground(new Color(39, 174, 96));
				trayIcon.getPopupMenu().getItem(2).setLabel("Status: Connected");

				// this.displayIconMessage("Hydra", "Connected to Server!", MessageType.INFO,
				// IconMessageMode.ALWAYS);

			} else {
				this.serverIndicator.setText("Server Status : Down");
				this.serverIndicator.setBackground(Color.RED);
				trayIcon.getPopupMenu().getItem(2).setLabel("Status: Disonnected");

				// this.displayIconMessage("Hydra", "Disconnected to Server!",
				// MessageType.WARNING, IconMessageMode.PERIODIC);

			}
		});

	}

	@Override
	public void displayIconMessage(String caption, String message, MessageType messageType, IconMessageMode mode) {
		// TODO Auto-generated method stub

		if (mode.equals(IconMessageMode.ALWAYS)) {
			this.trayIcon.displayMessage(caption, message, messageType);
		} else {
			Long lapsed = Calendar.getInstance().getTimeInMillis() - this.iconShowMessageTimestamp;

			if (lapsed > 20000 && !this.mainWindow.isVisible()) {
				this.trayIcon.displayMessage(caption, message, messageType);
				this.iconShowMessageTimestamp = Calendar.getInstance().getTimeInMillis();
			}

		}

	}

	@Override
	public void displaySystemLog(String line) {
		// String lineText = String.format("%s%n", line);
		String lineText = String.format("[%s] %s%n", this.simpleDateFormat.format(Calendar.getInstance().getTime()),
				line);
		SwingUtilities.invokeLater(() -> {
			if (this.logArea.getLineCount() > 6) {
				logArea.setText("");
			}

			this.logArea.append(lineText);
		});

	}

	@Override
	public void updateIsWorkerActive(boolean isWorking) {

		SwingUtilities.invokeLater(() -> {
			if (isWorking) {
				this.workerIndicator.setText("Worker Status : Running");
				this.workerIndicator.setBackground(new Color(39, 174, 96));

			} else {
				this.workerIndicator.setText("Worker Status : Idle");
				this.workerIndicator.setBackground(new Color(243, 156, 18));
			}
		});

	}

	@Override
	public void updateMemoryUsages(Long freeMem, Long totalMem) {
		totalMem = totalMem > 0 ? totalMem : freeMem;

		Integer freePercent = ((Double) ((totalMem.doubleValue() - freeMem.doubleValue()) / totalMem.doubleValue()
				* 100)).intValue();
		// this.memoryBar.setValue(freePercent);
		// this.memoryBar.setString("Memory Usage : " + freePercent + "%");

		SwingUtilities.invokeLater(() -> {

			this.memoryBar.setValue(freePercent);
			this.memoryBar.setString("Memory Usage : " + freePercent + "%");
		});

	}

	@Override
	public void updateSystemInfo(SystemInfo systemInfo) {

		CentralProcessor processor = systemInfo.getHardware().getProcessor();

		Double cpuUsage = processor.getSystemCpuLoad();
		String cpuUsageVal = String.format("%.2f", cpuUsage * 100);

		Integer cpuUsageText = ((Double) Double.parseDouble(cpuUsageVal)).intValue();

		GlobalMemory memory = systemInfo.getHardware().getMemory();

		Long availableMem = memory.getAvailable();
		Long totalMem = memory.getTotal();

		String usage = String.format("%.2f", 100 - (availableMem.doubleValue() / totalMem.doubleValue() * 100));

		Integer usageValue = ((Double) Double.parseDouble(usage)).intValue();

		SwingUtilities.invokeLater(() -> {
			this.cpuBar.setValue(cpuUsageText);
			this.cpuBar.setString("CPU Usage : " + cpuUsageVal + "%");

			this.memoryBar.setValue(usageValue);
			this.memoryBar.setString("Mem Usage : " + usage + "%");
		});

	}

	@Override
	public void updateClientInfo(JsonObject clientInfoJson) {

		SwingUtilities.invokeLater(() -> {
			if (clientInfoJson == null) {

				this.hostLabel.setText("Connecting...");
				this.hostLabel.setBackground(Color.RED);

			} else {
				this.hostLabel.setText(clientInfoJson.get("host").getAsString() + ", Client ID: "
						+ clientInfoJson.get("client_id").getAsString());

				this.hostLabel.setBackground(new Color(39, 174, 96));
			}
		});

	}

	@Override
	public void refreshTable(List<Object[]> objects) {

		SwingUtilities.invokeLater(() -> {
			DefaultTableModel model = (DefaultTableModel) this.systemInfoTable.getModel();

			JTable table = this.systemInfoTable;

			model.setRowCount(0);
			for (Object[] object : objects) {
				model.addRow(object);
			}

			TableColumnAdjuster tt = new TableColumnAdjuster(table);

			tt.adjustColumns();
		});

	}
}
