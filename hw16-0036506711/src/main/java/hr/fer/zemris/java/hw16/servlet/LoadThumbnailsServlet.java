package hr.fer.zemris.java.hw16.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw16.util.ImageDatabase;

@WebServlet("/servlet")
public class LoadThumbnailsServlet extends HttpServlet {

	private static final long serialVersionUID = -4182527742881463793L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.setContentType("image/png");
		String id = req.getParameter("id");
		BufferedImage thumbnail = ImageDatabase.loadThumbnailWithId(req, id);
		ImageIO.write(thumbnail, "png", resp.getOutputStream());
	}

}
