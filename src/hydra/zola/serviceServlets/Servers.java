package hydra.zola.serviceServlets;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaHelper;
import hydra.zola.model.HydraConnectionClient;

@Path("/Servers")
public class Servers {

	ZolaController zolaController = ZolaHelper.getInstance().getController();
	Gson gson = new Gson();
	Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStatus() {

		ConcurrentHashMap<String, HydraConnectionClient> clients = zolaController.getRepository().getClients();
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

		return gsonPretty.toJson(resArray);

	}
}
