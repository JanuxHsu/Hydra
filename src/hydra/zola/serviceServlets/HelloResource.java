package hydra.zola.serviceServlets;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

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