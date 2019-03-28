package hydra.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class HydraMessage {

	public static enum MessageType {
		P2P, BROADCAST, HEARTBEAT, FULLSYSINFO, REGISTER
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

	public MessageType getMessageType() {
		return this.messageType;
	}

	public JsonElement getMessageBody() {
		return this.message;
	}

}
