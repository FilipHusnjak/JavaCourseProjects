package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet that generates proper parameters so the result page can be shown
 * to the user.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 3042154876504834204L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> results = DAOProvider.getDao().getPollOptionsList(pollID);
		req.setAttribute("pollID", pollID);
		// Set results for jsp file
		req.setAttribute("results", results);
		// Get winners
		List<PollOption> winners = getWinners(results);
		// Set winners for jsp file
		req.setAttribute("winners", winners);
		// Dispatch the request
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Returns map with bands that have maximum number of votes.
	 * 
	 * @param resultParam
	 *        map that contains bands mapped with its voting results
	 * @return map with bands that have maximum number of votes
	 */
	private List<PollOption> getWinners(List<PollOption> results) {
		List<PollOption> winners = new LinkedList<>(results);
		winners.removeIf(o -> o.getVotes() != results.get(0).getVotes());
		return winners;
	}
	
}
