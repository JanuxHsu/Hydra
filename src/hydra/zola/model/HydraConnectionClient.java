package hydra.zola.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonObject;

import hydra.model.HydraMessage;
import hydra.model.TableColumn;
import hydra.model.HydraMessage.MessageType;
import hydra.zola.core.RequestThread;

public class HydraConnectionClient {
	public enum ClientType {
		VIPER, HYDRA, UNKNOWN
	}

	@TableColumn(columName = "Client")
	private final String clientID;

	@TableColumn(columName = "Ver.")
	private String clientVersion;

	@TableColumn(columName = "Client IP")
	InetAddress clientAddr;

	ClientType clientType = ClientType.UNKNOWN;

	private final RequestThread clientThread;

	@TableColumn(columName = "Last RecvTime")
	Date lastUpdateTime = Calendar.getInstance().getTime();

	final Date acceptedTime;

	@TableColumn(columName = "Last Message")
	String message = "---";

	String systemInfoMessage = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public HydraConnectionClient(String clientId, String clientVersion, RequestThread clientThread, Date acceptedTime,
			InetAddress inetAddress) {

		this.clientID = clientId;
		this.clientVersion = clientVersion;
		this.clientThread = clientThread;
		this.acceptedTime = acceptedTime;
		this.clientAddr = inetAddress;

	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void updateClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public ClientType getClientType() {
		return this.clientType;
	}

	public String getFormattedAcceptTime() {
		return sdf.format(this.acceptedTime);
	}
	
	public String getFormattedLastUpdateTime() {
		return sdf.format(this.lastUpdateTime);
	}

	public void updateLastMessage(String msg) {
		this.message = msg;
		this.lastUpdateTime = Calendar.getInstance().getTime();
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

	public InetAddress getClientAddress() {
		return this.clientAddr;
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

	public String getClientVersion() {

		return this.clientVersion;
	}

	public void setClientInfo(ClientType clientType, String clientVersion) {
		this.clientType = clientType;
		this.clientVersion = clientVersion;

	}

	public void setClientSystemInfo(String msg) {
		this.systemInfoMessage = msg;
	}

	public String getClientSystemInfo() {
		return this.systemInfoMessage;
	}

	public void disconnect(boolean isShutDown) {

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("action", isShutDown ? "shutdown" : "reset");

		HydraMessage hydraMessage = new HydraMessage(jsonObject, this.clientID, MessageType.MANAGEMENT);

		System.out.println(hydraMessage.toString());

		try {
			this.clientThread.sendMessage(hydraMessage.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.clientThread.close();
	}
}
