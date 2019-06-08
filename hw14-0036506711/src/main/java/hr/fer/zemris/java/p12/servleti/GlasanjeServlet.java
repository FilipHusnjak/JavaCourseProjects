package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet used to load all bands from the file and create proper parameters.
 * Once it loads records this servlet dispatches the request to the glasanjeIndex.jsp
 * file.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

	private static final long serialVersionUID = -2086931568597427727L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("pollID"));
		req.setAttribute("poll", DAOProvider.getDao().getPoll(pollID));
		req.setAttribute("pollOptions", DAOProvider.getDao().getPollOptionsList(pollID));
		req.setAttribute("pollID", pollID);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
