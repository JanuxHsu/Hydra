package hydra_framework.zola.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hydra_framework.model.HydraMessage;
import hydra_framework.model.HydraMessage.MessageType;


public class RequestThread implements Runnable {
	private final Socket clientSocket;

	final String clientId;

	final ZolaController zolaController;
	DataInputStream input = null;
	DataOutputStream output = null;

	private boolean runFlag = true;

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

		String connectionText = String.format("%s New Client Connected! ClientId: %s", clientSocket.getRemoteSocketAddress(),
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

			String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":"
					+ this.zolaController.httpServicePort + "/api/clients/download";
			responseJson.addProperty("action", "update");
			responseJson.addProperty("currentVersion", ZolaConfig.hydraClientVersion.trim());
			responseJson.addProperty("file_url", url);
			HydraMessage echoMessage = new HydraMessage(responseJson, clientId, MessageType.REGISTER);
			// System.out.println(new
			// GsonBuilder().setPrettyPrinting().create().toJson(updateMessage));

			output.writeUTF(new Gson().toJson(echoMessage));
			output.flush();
			String message;
			while (this.runFlag && (message = input.readUTF()) != null) {

				this.zolaController.OnRecvClientMessage(this.clientId, message);

			}
		} catch (IOException e) {

			// e.printStackTrace();

			System.err.println("Client disconnected!");
		}

		this.zolaController.removeClient(this.clientId);

	}

	public void close() {
		this.runFlag = false;

	}
}