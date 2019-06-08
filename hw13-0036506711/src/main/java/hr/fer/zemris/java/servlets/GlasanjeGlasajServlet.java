package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that increments the voting result of the band with id provided
 * through request parameters. In other words this servlet updates result
 * file incrementing proper result record.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1854830379738735007L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		int ID = Integer.parseInt(req.getParameter("id"));
		// Fetches the results from the proper file
		Map<Integer, Integer> results = ServletUtil.loadResults(req);
		// Increments the value of the proper result and adds the record if it
		// does not exist
		results.merge(ID, 1, (oldV, value) -> oldV + value);
		// Stores the updated records back to the proper file
		ServletUtil.storeResults(req, results);
		// Redirects user to the voting result page
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
}
