package hydra.zola.core;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hydra.repository.ZolaServerRepository;
import hydra.zola.model.HydraConnectionClient;

public class HttpPathHandler {

	public enum HttpMethod {
		GET, POST
	}

	final ZolaServerRepository zolaServerRepository;

	final private HttpMethod httpMethod;

	private String response = "";

	public HttpPathHandler(ZolaServerRepository zolaServerRepository, String method, String url) {
		this.zolaServerRepository = zolaServerRepository;
		this.httpMethod = HttpMethod.valueOf(HttpMethod.class, method.toUpperCase());
		this.parseRequest(url);
	}

	public void parseRequest(String url) {

		switch (this.httpMethod) {
		case GET:
			this.handleGetAction(url);
			break;

		case POST:
			this.handlePostAction(url);
			break;

		default:
			response = "404 Page Not Found. Invalid Request\r\n";
			break;
		}

	}

	private void handlePostAction(String url) {
		// TODO Auto-generated method stub

	}

	public void handleGetAction(String url) {

		StringTokenizer pathTokens = new StringTokenizer(url, "/");

		ConcurrentHashMap<String, HydraConnectionClient> clients = this.zolaServerRepository.getClients();
		JsonElement res = null;
		switch (pathTokens.nextToken()) {
		case "servers":

			if (!pathTokens.hasMoreTokens()) {
				JsonArray resArray = new JsonArray();
				for (String clientId : clients.keySet()) {
					HydraConnectionClient clientItem = clients.get(clientId);

					try {
						JsonObject json = new JsonObject();

						json.addProperty("client_id", clientItem.getClientID());
						json.addProperty("client_name", clientItem.getClientAddress().getHostName());
						json.addProperty("client_address", clientItem.getClientAddress().getHostAddress());
						json.addProperty("client_version", clientItem.getClientVersion());

						resArray.add(json);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				res = resArray;
			} else {
				response = "404 Page Not Found. Invalid Request\r\n";
			}

			break;

		case "status":

			if (pathTokens.hasMoreTokens()) {
				JsonObject resObj = new JsonObject();
				String client_id = pathTokens.nextToken();
				HydraConnectionClient clientItem = clients.get(client_id);

				if (clients.containsKey(client_id)) {
					JsonObject json = new Gson().fromJson(clientItem.getMessage(), JsonObject.class);
					resObj = json.get("message").getAsJsonObject();
				} else {
					response = "404 Page Not Found. Invalid Request\r\n";
				}

				res = resObj;

			} else {
				JsonArray resArray = new JsonArray();
				for (String clientId : clients.keySet()) {
					HydraConnectionClient clientItem = clients.get(clientId);

					try {
						JsonObject json = new Gson().fromJson(clientItem.getMessage(), JsonObject.class);
						resArray.add(json.get("message").getAsJsonObject());
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				res = resArray;
			}

			break;

		default:
			break;
		}

		if (res != null) {

			this.response = new GsonBuilder().setPrettyPrinting().create().toJson(res);
		}

	}

	public String getResponse() {

		return this.response;
	}
}
