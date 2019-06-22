package hr.fer.zemris.java.hw16.rest;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import hr.fer.zemris.java.hw16.util.ImageDatabase;

@Path("/image")
public class GetImage {
	
	@GET
	public void getImage(@Context HttpServletRequest req, 
			@Context HttpServletResponse resp) throws IOException {
		String id = req.getParameter("id");
		BufferedImage image = ImageDatabase.loadImageWithId(req, id);
		ImageIO.write(image, "jpg", resp.getOutputStream());
	}
	
}
