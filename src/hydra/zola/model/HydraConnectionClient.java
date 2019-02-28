package hydra.zola.model;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hydra.zola.core.RequestThread;

public class HydraConnectionClient {
	public enum ClientType {
		VIPER, HYDRA, UNKNOWN
	}

	@TableColumn(columName = "Client")
	private final String clientID;

	@TableColumn(columName = "Client Addr.")
	InetAddress clientAddr;

	ClientType clientType = ClientType.UNKNOWN;

	private final RequestThread clientThread;

	Date lastUpdateTime;

	@TableColumn(columName = "Created time")
	final Date acceptedTime;

	@TableColumn(columName = "Last Message")
	String message = "Connected";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public HydraConnectionClient(String clientId, RequestThread clientThread, Date acceptedTime) {
		this.clientID = clientId;
		this.clientThread = clientThread;
		this.acceptedTime = acceptedTime;

	}

	public void updateClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public String getFormattedAcceptTime() {
		return sdf.format(this.acceptedTime);
	}

	public void updateLastMessage(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return this.message;
	}

	public RequestThread getClientThread() {
		return clientThread;
	}

	public String getClientID() {
		return clientID;
	}

	public static ArrayList<String> getTableCsolumn() {

		ArrayList<String> cols = new ArrayList<>();

		Field[] fields = HydraConnectionClient.class.getDeclaredFields();

		for (Field field : fields) {
			// System.out.println(field.getAnnotation(TableColumn.class));
			if (field.isAnnotationPresent(TableColumn.class)) {

				cols.add(field.getAnnotation(TableColumn.class).columName());
			}

		}
		return cols;
	}
}
