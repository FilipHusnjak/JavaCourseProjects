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

@Path("/imagesForTag")
public class ListImages {

	@GET
	@Produces("application/json")
	public Response getImages(@Context HttpServletRequest request) throws IOException {
		String tag = request.getParameter("tag");
		String[] images = ImageDatabase.imagesForTag(request, tag);
		Gson gson = new Gson();
		return Response.status(Status.OK).entity(gson.toJson(images)).build();
	}
	
}
