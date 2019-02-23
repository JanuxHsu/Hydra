package hydra.viper.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import hydra.viper.gui.ViperClientGui;

public class ViperClientTcpSocketImpl extends ViperClient {

	boolean running = true;

	public ViperClientTcpSocketImpl(String clientName, ViperClientGui gui) {
		super(clientName, gui);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		String host = "localhost";
		int port = 5987;
		Socket socket = null;

		System.out.println("請輸入Server端位址: " + host);

		socket = new Socket(host, port);
		DataInputStream input = null;
		DataOutputStream output = null;

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());

		while (running) {

			System.out.println("Input message: ");
			Scanner consoleInput = new Scanner(System.in);
			String tt = consoleInput.nextLine();
			System.out.println("Sending " + tt);

			output.writeUTF(tt);
			output.flush();

		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String messageText) {
		System.out.println("Message: " + messageText);

	}

}
