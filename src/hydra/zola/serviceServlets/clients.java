package hydra.zola.serviceServlets;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.ws.ResponseWrapper;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import hydra.zola.core.ZolaController;
import hydra.zola.core.ZolaHelper;
import hydra.zola.model.HydraConnectionClient;

@Path("/clients")
public class clients {

	ZolaController zolaController = ZolaHelper.getInstance().getController();
	Gson gson = new Gson();
	Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String doGetAllClients() {

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
				json.addProperty("client_connected_DT", clientItem.getFormattedAcceptTime());

				resArray.add(json);
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
		HydraConnectionClient clientItem = clients.get(client_id);
		if (clientItem != null) {

			try {
				JsonObject jsonObj = new JsonObject();

				jsonObj.addProperty("client_id", clientItem.getClientID());
				jsonObj.addProperty("client_name", clientItem.getClientAddress().getHostName());
				jsonObj.addProperty("client_address", clientItem.getClientAddress().getHostAddress());
				jsonObj.addProperty("client_version", clientItem.getClientVersion());
				jsonObj.addProperty("client_connected_DT", clientItem.getFormattedAcceptTime());

				json = gsonPretty.fromJson(jsonObj, JsonElement.class);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		return gsonPretty.toJson(json);
	}

	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ResponseWrapper()
	public javax.ws.rs.core.Response download() {
		File file = new File(FilenameUtils.concat(System.getProperty("user.dir"), "hydraClient.jar"));

		javax.ws.rs.core.Response res = null;
		if (file.exists()) {
			ResponseBuilder response = javax.ws.rs.core.Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + file.getName());

			res = response.build();
		}

		return res;

	}

	@GET
	@Path("/checkFile")
	@Produces(MediaType.TEXT_PLAIN)
	public String getClientVersion() {
		File file = new File(FilenameUtils.concat(System.getProperty("user.dir"), "hydraClient.jar"));

		return file.getPath() + " is " + (file.exists() ? "avalible!" : "not found");

	}
}
