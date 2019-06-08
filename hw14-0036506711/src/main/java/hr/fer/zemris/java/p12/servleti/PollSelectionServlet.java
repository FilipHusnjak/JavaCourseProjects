package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Servlet which loads polls from database and sets them as proper parameter
 * so the selected jsp file can display polls in a list.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/index.html")
public class PollSelectionServlet extends HttpServlet {

	private static final long serialVersionUID = 741855728020364009L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		List<Poll> polls = DAOProvider.getDao().getPollsList();
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/pollsList.jsp").forward(req, resp);
	}
	
}
