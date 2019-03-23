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

	ZolaServer hydraServer;
	DataInputStream input = null;
	DataOutputStream output = null;

	public RequestThread(ZolaServer hydraServer, String clientId, Socket clientSocket) {
		this.clientId = clientId;
		this.clientSocket = clientSocket;
		this.hydraServer = hydraServer;
	}

	public void sendMessage(String message) throws IOException {
		output.writeUTF(message);
		output.flush();
	}

	@Override
	public void run() {

		this.hydraServer.mainForm
				.writeLog(String.format("%s連線進來! ClientId: %s", clientSocket.getRemoteSocketAddress(), this.clientId));

		try {
			input = new DataInputStream(this.clientSocket.getInputStream());
			output = new DataOutputStream(this.clientSocket.getOutputStream());

			JsonObject responseJson = new JsonObject();

			responseJson.addProperty("host", InetAddress.getLocalHost().getHostName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String timestamp = sdf.format(Calendar.getInstance().getTime());
			responseJson.addProperty("host_recv_time", timestamp);
			responseJson.addProperty("messageType", "register");

			responseJson.addProperty("client_cnt", this.hydraServer.getRepository().getClients().size());
			responseJson.addProperty("client_id", this.clientId);

			output.writeUTF(new Gson().toJson(responseJson));
			output.flush();
			String message;
			while ((message = input.readUTF()) != null) {
				this.hydraServer.updateClientMessage(this.clientId, message);

//				if (message.equals("go")) {
//					this.hydraServer.broadcast(this.clientId, message);
//				}
				System.out.println(message);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.hydraServer.removeClient(this.clientId);

	}
}