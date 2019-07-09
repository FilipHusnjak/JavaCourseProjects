package hr.fer.zemris.java.hw16.rest;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw16.util.ImageDatabase;

/**
 * REST API class used to get retrieve all tags which images belong to.
 * 
 * @author Filip Husnjak
 */
@Path("/tags")
public class TagList {

	@GET
	@Produces("application/json")
	public Response getTags(@Context HttpServletRequest request) throws IOException {
		String[] tags = ImageDatabase.loadTags(request);
		Gson gson = new Gson();
		return Response.status(Status.OK).entity(gson.toJson(tags)).build();
	}

}
