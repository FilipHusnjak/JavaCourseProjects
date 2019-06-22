package hr.fer.zemris.java.blog.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet which redirects user to "/servleti/main"
 * 
 * @author Filip Husnjak
 */
@WebServlet("/index.jsp")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = -6459579847471246956L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
	
}
