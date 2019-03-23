package hydra.viper.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.SocketFactory;

public class ViperConnector implements Runnable {
	volatile boolean running = true;
	ViperController viperController;

	Socket socket;

	DataInputStream serverInputStream;
	DataOutputStream serverOutputStream;

	public ViperConnector(ViperController viperController) {
		this.viperController = viperController;
	}

	@Override
	public void run() {
		String host = this.viperController.ZolaServerHost;
		int port = this.viperController.ZolaServerPort;

		try {
			// this.socket =Socket.setSocketImplFactory();new Socket(host, port);
			this.socket = SocketFactory.getDefault().createSocket(host, port);

			this.serverInputStream = new DataInputStream(this.socket.getInputStream());
			this.serverOutputStream = new DataOutputStream(this.socket.getOutputStream());

			System.out.println("loop start");
			String line;
			while (running && (line = this.serverInputStream.readUTF()) != null) {

				System.out.println(line);
				this.viperController.systemLog(line);

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
//
//System.out.println("Input message: ");
//Scanner consoleInput = new Scanner(System.in);
//String tt = consoleInput.nextLine();
//System.out.println("Sending " + tt);
//
//output.writeUTF(tt);
//output.flush();