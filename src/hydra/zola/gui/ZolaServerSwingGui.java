package hydra.zola.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import hydra.gui.utils.BasicLabeledInputGroup;
import hydra.gui.utils.GridBagLayoutHelper;
import hydra.gui.utils.TableColumnAdjuster;
import hydra.gui.utils.ZolaTableModel;
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

	ConcurrentHashMap<String, String> row_client_map = new ConcurrentHashMap<>();

	final ZolaController zolaController;

	JPanel operaionPanel;

	JLabel operationPanelTitleLabel;

	Map<String, JTextField> clientInfoinputGroupMap = new LinkedHashMap<>();

	public ZolaServerSwingGui(ZolaController zolaController) {
		this.zolaController = zolaController;
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

		JPanel windowPanel = new JPanel(new BorderLayout());

		windowPanel.add(topPanel(windowPanel), BorderLayout.NORTH);
		windowPanel.add(getOperationPanel(windowPanel), BorderLayout.EAST);
		windowPanel.add(getCenterPanel(windowPanel), BorderLayout.CENTER);
		windowPanel.add(getServerLogPanel(windowPanel), BorderLayout.SOUTH);
		// window.add(windowPanel);
		window.setContentPane(windowPanel);
		window.pack();
		this.mainWindow = window;
	}

	@Override
	public void setTitle(String name) {

		SwingUtilities.invokeLater(() -> {
			this.mainWindow.setTitle(name);
		});

	}

	private JPanel topPanel(JPanel windowPanel) {
		JPanel topPanel = new JPanel(new GridLayout(2, 2));
		JLabel serverServiceInfoInfo = new JLabel("Resolving...");
		serverServiceInfoInfo.setOpaque(true);
		serverServiceInfoInfo.setForeground(Color.white);
		serverServiceInfoInfo.setBackground(Color.red);
		serverServiceInfoInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

		this.serverInfoLabel = serverServiceInfoInfo;

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

		JButton kickClientBtn = new JButton("Close All");
		kickClientBtn.setContentAreaFilled(false);
		kickClientBtn.setFont(defaultFont);
		kickClientBtn.addActionListener(new buttonListener());
		kickClientBtn.setPreferredSize(new Dimension(100, 25));

		clientActionPanel.add(kickClientBtn, BorderLayout.EAST);

		clientActionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		topPanel.add(serverServiceInfoInfo);

		topPanel.add(clientActionPanel);
		return topPanel;
	}

	private JPanel getOperationPanel(JPanel windowPanel) {
		JPanel operationContainerPanel = new JPanel(new BorderLayout());

		operationContainerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		operationContainerPanel.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				operationContainerPanel.setPreferredSize(new Dimension(350, windowPanel.getHeight()));

			}

			@Override
			public void componentResized(ComponentEvent e) {
				operationContainerPanel.setPreferredSize(new Dimension(350, windowPanel.getHeight()));

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JPanel operaionPanel = new JPanel(new BorderLayout());
		operaionPanel.setOpaque(true);
		operaionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		operaionPanel.setBackground(new Color(210, 218, 226));

		JPanel opTopPanel = new JPanel(new GridBagLayout());

		JLabel titleLabel = new JLabel("-------");
		this.operationPanelTitleLabel = titleLabel;
		JButton closeBtn = new JButton("X");
		closeBtn.setContentAreaFilled(false);
		closeBtn.setFocusPainted(false);
		closeBtn.setToolTipText("Close Window");
		closeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> {
					operationContainerPanel.setVisible(false);
				});

			}
		});

		closeBtn.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
		closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		closeBtn.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		titleLabel.setForeground(Color.WHITE);

		GridBagConstraints bag1 = new GridBagConstraints();
		bag1.gridx = 0;
		bag1.gridy = 0;
		bag1.gridwidth = 4;
		bag1.gridheight = 1;
		bag1.weightx = 1;
		bag1.weighty = 0;
		bag1.fill = GridBagConstraints.BOTH;
		bag1.anchor = GridBagConstraints.EAST;
		opTopPanel.add(titleLabel, bag1);
		GridBagConstraints bag2 = new GridBagConstraints();
		bag2.gridx = 4;
		bag2.gridy = 0;
		bag2.gridwidth = 1;
		bag2.gridheight = 1;
		bag2.weightx = 0;
		bag2.weighty = 0;
		bag2.fill = GridBagConstraints.NONE;
		bag2.anchor = GridBagConstraints.EAST;
		opTopPanel.add(closeBtn, bag2);

		opTopPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		opTopPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.GRAY));
		opTopPanel.setOpaque(true);
		opTopPanel.setBackground(new Color(30, 39, 46));

		operaionPanel.add(opTopPanel, BorderLayout.NORTH);

		GridBagLayout gridBagLayout = new GridBagLayout();
		JPanel cmdOperationPanel = new JPanel(gridBagLayout);
		cmdOperationPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		// instantiates Border panels.
		JPanel clientInfoPanel = new JPanel(new GridLayout(1, 1));

		clientInfoPanel.setBorder(BorderFactory.createTitledBorder("Client Information"));

		this.clientInfoinputGroupMap.put("ClientID", new JTextField());
		this.clientInfoinputGroupMap.put("Host", new JTextField());
		this.clientInfoinputGroupMap.put("IP Address", new JTextField());

		clientInfoPanel.add(new BasicLabeledInputGroup(this.clientInfoinputGroupMap));

		JPanel cmdControllerPanel = new JPanel();
		cmdControllerPanel.setBorder(BorderFactory.createTitledBorder("Send Command"));

		// adding all panels to main contentPane.
		cmdOperationPanel.add(clientInfoPanel);
		cmdOperationPanel.add(cmdControllerPanel);

		// set constraints of each panel.
		GridBagLayoutHelper.makeConstraints(gridBagLayout, clientInfoPanel, 4, 1, 0, 0, 2.0, 1.0, 5);
		GridBagLayoutHelper.makeConstraints(gridBagLayout, cmdControllerPanel, 4, 3, 0, 2, 2.0, 8.0, 5);

//		cmdOperationPanel.add(new BasicLabeledInput("Working Dir", true));
//		cmdOperationPanel.add(new BasicLabeledInput("Command", false));

		// ###############################################################
		operaionPanel.add(cmdOperationPanel, BorderLayout.CENTER);
		operationContainerPanel.add(operaionPanel, BorderLayout.CENTER);

		// operationContainerPanel.setVisible(false);
		this.operaionPanel = operationContainerPanel;
		return operationContainerPanel;
	}

	private JPanel getCenterPanel(JPanel windowPanel) {
		JPanel centerPanel = new JPanel(new BorderLayout());

		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		
//		JPanel TablePanel = new JPanel(new GridLayout(1, 1));
//		TablePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));

		ZolaTableModel tableModel = new ZolaTableModel();
		tableModel.addColumn("No.");
		for (String colName : HydraConnectionClient.getTableCsolumn()) {
			tableModel.addColumn(colName);

		}

		JTable resultTable = new JTable(tableModel);

		resultTable.setAutoCreateRowSorter(true);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		resultTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int rowIdx = target.getSelectedRow();

				@SuppressWarnings("unchecked")
				Vector<String> rowdata = (Vector<String>) tableModel.getDataVector().get(rowIdx);

				zolaController.setupClientOperaion(rowdata.get(0));

			}
		});

		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane = new JScrollPane(resultTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

		this.clientListTable = resultTable;

		centerPanel.add(scrollPane, BorderLayout.CENTER);

		// centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

		return centerPanel;

	}

	private JPanel getServerLogPanel(JPanel windowPanel) {

		JPanel serverLogPanel = new JPanel(new GridLayout(1, 1));
		JTextArea logArea = new JTextArea(4, 10);

		DefaultCaret caret = (DefaultCaret) logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		logArea.setEditable(false);
		logArea.setFont(defaultFont);

		this.loggingBox = logArea;

		serverLogPanel.add(new JScrollPane(logArea));
		serverLogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		// serverLogPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

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
	public void refreshTable(List<List<String>> rowList) {

		try {

			DefaultTableModel model = (DefaultTableModel) this.clientListTable.getModel();
			JTable table = this.clientListTable;
			TableColumnAdjuster tableColumnAdjuster = new TableColumnAdjuster(table);

			@SuppressWarnings("unchecked")
			Vector<Vector<String>> dataVector = model.getDataVector();

			dataVector.clear();
			row_client_map.clear();

			rowList.stream().forEach(row -> {
				Vector<String> vect = row.stream().map(columnVal -> {

					return columnVal.trim();
				}).collect(Collectors.toCollection(Vector::new));
				dataVector.add(vect);

			});

			SwingUtilities.invokeLater(() -> {

				tableColumnAdjuster.adjustColumns();

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@Override
	public void setupOperationPanel(HydraConnectionClient client) {

		SwingUtilities.invokeLater(() -> {
			this.operaionPanel.setVisible(true);

			String host = client.getClientAddress() == null ? "---" : client.getClientAddress().getHostName();
			String ip = client.getClientAddress() == null ? "---" : client.getClientAddress().getHostAddress();
			operationPanelTitleLabel.setText(host);
			operationPanelTitleLabel.setToolTipText(client.getClientID());

			this.clientInfoinputGroupMap.get("ClientID").setText(client.getClientID());
			this.clientInfoinputGroupMap.get("Host").setText(host);
			this.clientInfoinputGroupMap.get("IP Address").setText(ip);

			this.clientInfoinputGroupMap.values().stream().forEach(item -> {
				item.setEditable(false);
			});

		});

	}

}