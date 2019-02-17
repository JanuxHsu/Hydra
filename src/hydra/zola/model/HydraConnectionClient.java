package hydra.zola.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import hydra.zola.core.RequestThread;

public class HydraConnectionClient {

	public final String clientID;
	public final RequestThread clientThread;
	public final Date acceptedTime;

	String message = "Connected";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public HydraConnectionClient(String clientId, RequestThread clientThread, Date acceptedTime) {
		this.clientID = clientId;
		this.clientThread = clientThread;
		this.acceptedTime = acceptedTime;
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
}
