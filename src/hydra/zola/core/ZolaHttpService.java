package hydra.zola.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hydra.hydra.core.HydraClient;
import hydra.zola.model.HydraConnectionClient;

public class ZolaHttpService implements Runnable {

	final ZolaController zolaController;

	public ZolaHttpService(ZolaController zolaController) {
		this.zolaController = zolaController;
	}

	@Override
	public void run() {

		ServerSocket serverSocket = null;
		try {

			int port = 8080;

			serverSocket = new ServerSocket(port);
			// Now enter an infinite loop, waiting for & handling connections.
			while (true) {
				// Wait for a client to connect. The method will block;
				// when it returns the socket will be connected to the client
				Socket client = serverSocket.accept();

				// Get input and output streams to talk to the client
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream());

				// Start sending our reply, using the HTTP 1.1 protocol
				out.print("HTTP/1.1 200 \r\n"); // Version & status code
				out.print("Content-Type: application/json\r\n"); // The type of data
				out.print("Connection: close\r\n"); // Will close stream
				out.print("\r\n"); // End of headers

				String line = in.readLine();

				try {
					String[] tokens = line.split(" ");

					String method = tokens[0];
					String req_path = tokens[1];

					if (method.equals("GET") && req_path.equals("/status/servers")) {

						ConcurrentHashMap<String, HydraConnectionClient> clients = zolaController.zolaServerRepository
								.getClients();

						JsonArray res = new JsonArray();

						for (String clientId : clients.keySet()) {
							HydraConnectionClient clientItem = clients.get(clientId);

							try {
								JsonObject json = new Gson().fromJson(clientItem.getMessage(), JsonObject.class);
								res.add(json.get("message").getAsJsonObject());
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						out.print(new GsonBuilder().setPrettyPrinting().create().toJson(res));

					} else {
						out.print("404 Page Not Found. Invalid Request\r\n");
					}

				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					// Close socket, breaking the connection to the client, and
					// closing the input and output streams
					out.close(); // Flush and close the output stream
					in.close(); // Close the input stream
					client.close(); // Close the socket itself
				}

			}
		}
		// If anything goes wrong, print an error message
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("Usage: java HttpMirror <port>");
		} finally {

			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
