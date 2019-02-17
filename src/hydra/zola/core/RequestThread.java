package hydra.zola.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
		output.writeUTF(String.format("%s send you [%s]!\n", this.clientId, message));
		output.flush();
	}

	@Override
	public void run() {

		this.hydraServer.mainForm
				.writeLog(String.format("%s連線進來! ClientId: %s", clientSocket.getRemoteSocketAddress(), this.clientId));

		try {
			input = new DataInputStream(this.clientSocket.getInputStream());
			output = new DataOutputStream(this.clientSocket.getOutputStream());
			output.writeUTF(String.format("Hi, %s!\n", clientSocket.getRemoteSocketAddress()));
			output.flush();
			String message;
			while ((message = input.readUTF()) != null) {
				this.hydraServer.updateClientMessage(this.clientId, message);

				if (message.equals("go")) {
					this.hydraServer.broadcast(message);
				}
				System.out.println(message);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		this.hydraServer.removeClient(this.clientId);

	}
}