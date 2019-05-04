package hydra.zola.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import hydra.gui.utils.HydraTableCellRender;
import hydra.gui.utils.TableColumnAdjuster;
import hydra.zola.core.ZolaController;
import hydra.zola.model.HydraConnectionClient;

public class ZolaServerSwingGui implements ZolaServerGui {

	protected JFrame mainWindow;
	Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	JLabel serverInfoLabel;

	JTextArea loggingBox;

	JTable clientListTable;

	JLabel threadPoolStatus;

	JLabel httpServiceStatus;

	public ZolaServerSwingGui(ZolaController zolaController) {
		JFrame window = new JFrame();
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(800, 500));
		window.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(this.getClass().getClassLoader().getResource("resources/hydra64.png")));
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(getServerInfoPanel(), BorderLayout.CENTER);

		// mainPanel.add(getServerLogPanel(), BorderLayout.SOUTH);

		window.add(mainPanel, BorderLayout.CENTER);
		window.add(getServerLogPanel(), BorderLayout.SOUTH);
		window.pack();
		this.mainWindow = window;
	}

	@Override
	public void setTitle(String name) {
		this.mainWindow.setTitle(name);
	}

	private JPanel getServerInfoPanel() {

		JPanel serverInfoPanel = new JPanel(new BorderLayout());

		JPanel testPanel = new JPanel(new GridLayout(1, 2));

		JLabel serverServiceInfoInfo = new JLabel("Resolving...");
		serverServiceInfoInfo.setOpaque(true);
		serverServiceInfoInfo.setForeground(Color.white);
		serverServiceInfoInfo.setBackground(Color.red);
		serverServiceInfoInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

		this.serverInfoLabel = serverServiceInfoInfo;

		testPanel.add(serverServiceInfoInfo);

		JPanel paddedPanel = new JPanel(new BorderLayout());

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("No.");
		for (String colName : HydraConnectionClient.getTableCsolumn()) {
			tableModel.addColumn(colName);

		}

		JTable resultTable = new JTable(tableModel);

		// resultTable.setFont(defaultFont);

		// resultTable.getTableHeader().setFont(defaultFont);

		resultTable.setAutoCreateRowSorter(true);
		HydraTableCellRender colorRenderer = new HydraTableCellRender();
		resultTable.setDefaultRenderer(Object.class, colorRenderer);

		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		this.clientListTable = resultTable;
		this.clientListTable.setPreferredScrollableViewportSize(new Dimension(0, 300));

		JScrollPane scrollPane = new JScrollPane(resultTable);

		paddedPanel.add(scrollPane, BorderLayout.CENTER);

		paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

		JPanel clientActionPanel = new JPanel(new BorderLayout());

		JPanel threadPanel = new JPanel(new GridLayout(1, 2, 2, 0));

		threadPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));

		JLabel httpServiceLabel = new JLabel("Checking...");

		httpServiceLabel.setOpaque(true);
		httpServiceLabel.setBackground(new Color(52, 31, 151));
		httpServiceLabel.setForeground(Color.white);
		httpServiceLabel.setHorizontalAlignment(JLabel.CENTER);
		this.httpServiceStatus = httpServiceLabel;
		threadPanel.add(httpServiceLabel);

		JLabel threadPoolLabel = new JLabel("Checking...");
		threadPoolLabel.setOpaque(true);
		threadPoolLabel.setBackground(new Color(238, 90, 36));
		threadPoolLabel.setForeground(Color.white);
		threadPoolLabel.setHorizontalAlignment(JLabel.CENTER);
		this.threadPoolStatus = threadPoolLabel;
		threadPanel.add(threadPoolLabel);

		clientActionPanel.add(threadPanel, BorderLayout.CENTER);

		JButton kickClient = new JButton("Close All");
		kickClient.setContentAreaFilled(false);
		kickClient.setFont(defaultFont);
		kickClient.addActionListener(new buttonListener());
		kickClient.setPreferredSize(new Dimension(100, 25));

		clientActionPanel.add(kickClient, BorderLayout.EAST);

		clientActionPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));

		paddedPanel.add(clientActionPanel, BorderLayout.SOUTH);

		serverInfoPanel.add(testPanel, BorderLayout.NORTH);

		serverInfoPanel.add(paddedPanel, BorderLayout.CENTER);

		return serverInfoPanel;

	}

	private JPanel getServerLogPanel() {

		JPanel serverLogPanel = new JPanel(new BorderLayout());
		JTextArea logArea = new JTextArea();

		DefaultCaret caret = (DefaultCaret) logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		logArea.setEditable(false);

		logArea.setFont(defaultFont);

		this.loggingBox = logArea;

		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setMaximumSize(new Dimension(0, 30));
		serverLogPanel.add(scrollPane, BorderLayout.CENTER);
		serverLogPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
		serverLogPanel.setMaximumSize(new Dimension(0, 30));
		return serverLogPanel;

	}

	public void show() {
		this.mainWindow.setLocationRelativeTo(null);
		this.mainWindow.setVisible(true);
	}

	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		System.exit(0);

	}

	@Override
	public void writeLog(String logText) {

		JTextArea logger = this.loggingBox;

		String logText2 = String.format("[%s] %s%n", this.simpleDateFormat.format(Calendar.getInstance().getTime()),
				logText);

		SwingUtilities.invokeLater(() -> {
			if (logger.getLineCount() > 10) {
				logger.setText("");
			}
			logger.append(logText2);
		});

		System.out.print(logText2);

	}

	@Override
	public void refreshTable(List<Object[]> objects) {

		DefaultTableModel model = (DefaultTableModel) this.clientListTable.getModel();
		JTable table = this.clientListTable;

		// model.setDataVector(objects,
		// HydraConnectionClient.getTableCsolumn().toArray());

		SwingUtilities.invokeLater(() -> {
			// model.getDataVector().clear();
			model.setRowCount(0);
			for (Object[] object : objects) {
				model.addRow(object);
			}

			TableColumnAdjuster tt = new TableColumnAdjuster(table);

			tt.adjustColumns();

		});

	}

	@Override
	public void setServiceInfo(String infoText) {

		SwingUtilities.invokeLater(() -> {
			this.serverInfoLabel.setText(infoText);
			this.serverInfoLabel.setBackground(new Color(52, 31, 151));

		});

	}

	@Override
	public void updateThreadPoolStatus(String status) {
		SwingUtilities.invokeLater(() -> {
			this.threadPoolStatus.setText(status);
		});

	}

	@Override
	public void updateHttpServiceStatus(String status) {
		SwingUtilities.invokeLater(() -> {
			this.httpServiceStatus.setText(status);
		});
	}

}
