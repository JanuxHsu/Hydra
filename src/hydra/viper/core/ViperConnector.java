package hydra.viper.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ViperConnector implements Runnable {
	volatile boolean running = true;
	ViperController viperController;

	Socket socket;

	public ViperConnector(ViperController viperController) {
		this.viperController = viperController;
	}

	@Override
	public void run() {
		String host = "localhost";
		int port = 5987;
		Socket socket = null;

		DataInputStream input = null;
		DataOutputStream output = null;
		try {
			this.socket = new Socket(host, port);

			input = new DataInputStream(this.socket.getInputStream());
			output = new DataOutputStream(this.socket.getOutputStream());

			System.out.println("loop start");
			String line;
			while (running && (line = input.readUTF()) != null) {

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

}
//
//System.out.println("Input message: ");
//Scanner consoleInput = new Scanner(System.in);
//String tt = consoleInput.nextLine();
//System.out.println("Sending " + tt);
//
//output.writeUTF(tt);
//output.flush();