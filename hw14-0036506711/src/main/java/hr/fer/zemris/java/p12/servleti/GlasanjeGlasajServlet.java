package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet that increments the voting result of the band with id provided
 * through request parameters. In other words this servlet updates result
 * file incrementing proper result record.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1854830379738735007L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		long ID = Long.parseLong(req.getParameter("id"));
		long pollID = Long.parseLong(req.getParameter("pollID"));
		// Fetches the results from the proper file
		PollOption option = DAOProvider.getDao().getPollOption(ID);
		DAOProvider.getDao().updatePollOption(option.getID(), option.vote());
		// Redirects user to the voting result page
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
	}
	
}
