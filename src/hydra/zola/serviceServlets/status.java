package hydra.zola.serviceServlets;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaHelper;
import hydra.zola.model.HydraConnectionClient;

@Path("/status")
public class status {

	ZolaHelper zolaHelper = ZolaHelper.getInstance();
	ZolaController zolaController = zolaHelper.getController();
	Gson gson = new Gson();
	Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String doGetAllStatus() {

		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaController.getRepository().getClients();
		JsonElement res = null;
		JsonArray resArray = new JsonArray();
		for (String clientId : clients.keySet()) {
			HydraConnectionClient clientItem = clients.get(clientId);

			try {
				JsonElement json = new Gson().fromJson(clientItem.getMessage(), JsonElement.class);
				if (json.isJsonObject()) {
					resArray.add(json.getAsJsonObject().get("message").getAsJsonObject());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		res = resArray;
		return gsonPretty.toJson(res);
	}

	@GET
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public String hello(@PathParam("param") String client_id) {
		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaController.getRepository().getClients();
		JsonElement json = null;
		if (clients.containsKey(client_id)) {
			json = gsonPretty.fromJson(clients.get(client_id).getClientSystemInfo(), JsonElement.class);
		}

		return gsonPretty.toJson(json);
	}

}