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

import hr.fer.zemris.java.hw16.model.Picture;
import hr.fer.zemris.java.hw16.util.ImageDatabase;

/**
 * REST API class used to get retrieve picture object with specified ID given through
 * request parameter.
 * 
 * @author Filip Husnjak
 */
@Path("/picture")
public class GetPicture {

	@GET
	@Produces("application/json")
	public Response getPicture(@Context HttpServletRequest req) throws IOException {
		String id = req.getParameter("id");
		Picture picture = ImageDatabase.loadPictureForId(req, id);
		Gson gson = new Gson();
		return Response.status(Status.OK).entity(gson.toJson(picture)).build();
	}
	
}
