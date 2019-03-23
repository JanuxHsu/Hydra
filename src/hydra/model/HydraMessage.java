package hydra.model;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class HydraMessage implements Serializable {

	private static final long serialVersionUID = 717594081457579756L;

	public static enum MessageType {
		P2P, BROADCAST, HEARTBEAT, SYSINFO, REGISTER
	};

	JsonElement message;
	String destination;
	String source;
	MessageType messageType;

	public HydraMessage(JsonElement message, String destination, MessageType messageType) {
		this.message = message;
		this.destination = destination;
		this.messageType = messageType;

	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {

		return new Gson().toJson(this, HydraMessage.class);
	}

}
