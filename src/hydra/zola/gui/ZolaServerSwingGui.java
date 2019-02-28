package hydra.zola.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import hydra.gui.utils.TableColumnAdjuster;
import hydra.zola.model.HydraConnectionClient;

public class ZolaServerSwingGui implements ZolaServerGui {

	protected JFrame mainWindow;

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	JTextArea loggingBox;

	JTable clientListTable;

	public ZolaServerSwingGui() {
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
		window.setPreferredSize(new Dimension(800, 400));
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(getServerInfoPanel(), BorderLayout.NORTH);

		mainPanel.add(getServerLogPanel(), BorderLayout.CENTER);
		window.add(mainPanel, BorderLayout.CENTER);

		window.pack();
		this.mainWindow = window;
	}

	@Override
	public void setTitle(String name) {
		this.mainWindow.setTitle(name);
	}

	private JPanel getServerInfoPanel() {
		String hostName = "Unknown Host";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		JPanel serverInfoPanel = new JPanel(new BorderLayout());

		JPanel testPanel = new JPanel(new GridLayout(1, 2));

		JLabel basicServerInfo = new JLabel("Host: " + hostName);

		testPanel.add(basicServerInfo);

		JPanel paddedPanel = new JPanel(new BorderLayout());

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("No.");
		for (String colName : HydraConnectionClient.getTableCsolumn()) {
			tableModel.addColumn(colName);

		}

		JTable resultTable = new JTable(tableModel);

		resultTable.setAutoCreateRowSorter(true);

//		resultTable.getColumnModel().getColumn(0).setPreferredWidth(15);
//		resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
//		resultTable.getColumnModel().getColumn(2).setPreferredWidth(60);

		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		this.clientListTable = resultTable;
		this.clientListTable.setPreferredScrollableViewportSize(new Dimension(0, 120));

		JScrollPane scrollPane = new JScrollPane(resultTable);

		paddedPanel.add(scrollPane, BorderLayout.CENTER);

		paddedPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

		JPanel clientActionPanel = new JPanel(new BorderLayout());

		JButton kickClient = new JButton("Kick");
		kickClient.setPreferredSize(new Dimension(100, 25));
		
		clientActionPanel.add(kickClient, BorderLayout.EAST);
		
		clientActionPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

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

		this.loggingBox = logArea;
		serverLogPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
		serverLogPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

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
		this.loggingBox.append(logText + "\n");
		System.out.println(logText);

	}

	@Override
	public void refreshTable(List<Object[]> objects) {
		DefaultTableModel model = (DefaultTableModel) this.clientListTable.getModel();
		model.setRowCount(0);
		for (Object[] object : objects) {
			model.addRow(object);
		}

		TableColumnAdjuster tt = new TableColumnAdjuster(this.clientListTable);

		tt.adjustColumns();
	}

}
