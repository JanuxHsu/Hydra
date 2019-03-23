package hydra.hydra.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.SocketFactory;

public class HydraConnector implements Runnable {
	volatile boolean running = true;
	HydraController hydraController;

	Socket socket;

	DataInputStream serverInputStream;
	DataOutputStream serverOutputStream;

	public HydraConnector(HydraController hydraController) {
		this.hydraController = hydraController;
	}

	@Override
	public void run() {
		String host = hydraController.ZolaServerHost;
		int port = hydraController.ZolaServerPort;

		try {
			this.socket = SocketFactory.getDefault().createSocket(host, port);

			this.serverInputStream = new DataInputStream(this.socket.getInputStream());
			this.serverOutputStream = new DataOutputStream(this.socket.getOutputStream());

			System.out.println("loop start");
			String line;
			while (running && (line = this.serverInputStream.readUTF()) != null) {

				System.out.println(line);
				this.hydraController.updateRecv(line);
				// this.hydraController.systemLog(line);

			}
			System.out.println("loop end");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Job closed");
		this.hydraController.connectionClose();

	}

	public void shutDown() {
		this.running = false;
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String messageText) {
		if (socket != null && !socket.isClosed()) {
			try {
				this.serverOutputStream.writeUTF(messageText);
				this.serverOutputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}