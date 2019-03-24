package hydra.zola.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RequestThread implements Runnable {
	private final Socket clientSocket;

	final String clientId;

	final ZolaController zolaController;
	DataInputStream input = null;
	DataOutputStream output = null;

	public RequestThread(ZolaController zolaController, String clientId, Socket clientSocket) {
		this.clientId = clientId;
		this.clientSocket = clientSocket;

		this.zolaController = zolaController;

	}

	public void sendMessage(String message) throws IOException {
		output.writeUTF(message);
		output.flush();
	}

	@Override
	public void run() {

		String connectionText = String.format("%s 連線進來! ClientId: %s", clientSocket.getRemoteSocketAddress(),
				this.clientId);
		this.zolaController.syslog(connectionText);

		try {
			input = new DataInputStream(this.clientSocket.getInputStream());
			output = new DataOutputStream(this.clientSocket.getOutputStream());

			JsonObject responseJson = new JsonObject();

			responseJson.addProperty("host", InetAddress.getLocalHost().getHostName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String timestamp = sdf.format(Calendar.getInstance().getTime());
			responseJson.addProperty("host_recv_time", timestamp);
			responseJson.addProperty("messageType", "register");

			responseJson.addProperty("client_cnt", this.zolaController.zolaServerRepository.getClients().size());
			responseJson.addProperty("client_id", this.clientId);

			output.writeUTF(new Gson().toJson(responseJson));
			output.flush();
			String message;
			while ((message = input.readUTF()) != null) {

				this.zolaController.updateClientMessage(this.clientId, message);
				// this.hydraServer.updateClientMessage(this.clientId, message);

//				if (message.equals("go")) {
//					this.hydraServer.broadcast(this.clientId, message);
//				}
				System.out.println(message);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.zolaController.removeClient(this.clientId);

	}
}