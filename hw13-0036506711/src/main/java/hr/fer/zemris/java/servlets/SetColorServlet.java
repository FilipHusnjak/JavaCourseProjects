package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that sets background color of the all sites in current session.
 * Background color is stored as session parameter.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/setcolor")
public class SetColorServlet extends HttpServlet {

	private static final long serialVersionUID = -188842571398802841L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String color = req.getParameter("bgcolor");
		color = color == null ? "white" : color;
		req.getSession().setAttribute("pickedBgCol", color);
		resp.sendRedirect(req.getContextPath() + "/index.jsp");
	}
	
}
