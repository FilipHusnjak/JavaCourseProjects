package hr.fer.zemris.java.blog.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to logout user.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 4058318010466196934L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.getSession().invalidate();
		resp.sendRedirect(req.getServletContext().getContextPath() + "/servleti/main");
	}
	
}
