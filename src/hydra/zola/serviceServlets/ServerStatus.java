package hydra.zola.serviceServlets;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaHelper;
import hydra.zola.model.HydraConnectionClient;

@Path("/ServerStatus")
public class ServerStatus {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {

		ZolaHelper zolaHelper = ZolaHelper.getInstance();
		ZolaController zolaController = zolaHelper.getController();

		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaController.getRepository().getClients();

		JsonElement res = null;

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

		return new GsonBuilder().setPrettyPrinting().create().toJson(res);
	}

	@GET
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String hello(@PathParam("param") String name) {

		return "aaa";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String helloUsingJson(String greeting) {
		return greeting + "\n";
	}

}