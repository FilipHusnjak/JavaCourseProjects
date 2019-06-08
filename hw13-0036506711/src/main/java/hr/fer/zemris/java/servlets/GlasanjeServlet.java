package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to load all bands from the file and create proper parameters.
 * Once it loads records this servlet dispatches the request to the glasanjeIndex.jsp
 * file.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = -2086931568597427727L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setAttribute("bands", ServletUtil.loadBands(req).values());
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
