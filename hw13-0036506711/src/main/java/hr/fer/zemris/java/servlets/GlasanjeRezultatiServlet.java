package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.ServletUtil.Band;

/**
 * Servlet that generates proper parameters so the result page can be shown
 * to the user.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 3042154876504834204L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<Integer, Integer> results = ServletUtil.loadResults(req);
		Map<Integer, Band> bands = ServletUtil.loadBands(req);
		// Create proper parameter for jsp file
		Map<Band, Integer> resultParam = getResultsParameter(results, bands);
		// Set parameter
		req.setAttribute("results", resultParam.entrySet());
		// Get winners
		Map<Band, Integer> winners = getWinnersParameter(resultParam);
		// Set winners for jsp file
		req.setAttribute("winners", winners.entrySet());
		// Dispatch the request
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Creates and returns map with voting result mapped to the band so it can be used
	 * in jsp file.
	 * 
	 * @param results
	 *        results map created from result file
	 * @param bands
	 *        map with bands mapped to proper IDs
	 * @return map with voting result mapped to the band
	 */
	private Map<Band, Integer> getResultsParameter(Map<Integer, Integer> results,
			Map<Integer, Band> bands) {
		return results.entrySet().stream().collect(
				Collectors.toMap(
						e -> bands.get(e.getKey()), 
						Map.Entry::getValue, 
						(e1, e2) -> e1, 
						LinkedHashMap::new));
	}
	
	/**
	 * Returns map with bands that have maximum number of votes.
	 * 
	 * @param resultParam
	 *        map that contains bands mapped with its voting results
	 * @return map with bands that have maximum number of votes
	 */
	private Map<Band, Integer> getWinnersParameter(Map<Band, Integer> resultParam) {
		Map<Band, Integer> winners = new LinkedHashMap<>();
		Integer winnerRes = null;
		for (Map.Entry<Band, Integer> entry: resultParam.entrySet()) {
			if (winnerRes != null && !winnerRes.equals(entry.getValue())) break;
			winnerRes = entry.getValue();
			winners.put(entry.getKey(), entry.getValue());
		}
		return winners;
	}
	
}
