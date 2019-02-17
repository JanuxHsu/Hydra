package model;

public class HydraMessage {

	String message;
	String destination;
	MessageType messageType;

	public HydraMessage(String message, String destination, MessageType messageType) {
		this.message = message;
		this.destination = destination;
		this.messageType = messageType;
	}

}
