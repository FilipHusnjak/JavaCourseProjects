package hr.fer.zemris.java.blog.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.util.Util;

/**
 * Servlet which lists all current users and provides login form for users that are
 * not logged yet.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = -4934671020667303200L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		setRequestAttributes(req, null, false);
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String nickname = req.getParameter("nickname");
		String password = req.getParameter("password");
		BlogUser user= loginUser(nickname, password);
		if (user == null) {
			setRequestAttributes(req, nickname, true);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		setSessionAttribs(req, user);
		resp.sendRedirect(req.getContextPath() + req.getServletPath());
	}
	
	/**
	 * Sets request attributes for dispatching the request.
	 * 
	 * @param req
	 *        request object used to dispatch the request
	 * @param nickname
	 *        nickname of the user that should be stored in request attributes
	 * @param error
	 */
	private void setRequestAttributes(HttpServletRequest req, String nickname, boolean error) {
		Util.provideLoggedUserInfo(req);
		req.setAttribute("nick", nickname);
		req.setAttribute("users", DAOProvider.getDAO().getBlogUsers());
		req.setAttribute("error", error);
	}
	
	/**
	 * Tries to login user and returns {@code null} if the login proccess failed
	 * or logged user object if it was successful.
	 * 
	 * @param nickname
	 *        provided nickname
	 * @param password
	 *        provided password
	 * @return {@link BlogUser} object of the logged user if it was successful
	 */
	private BlogUser loginUser(String nickname, String password) {
		if (nickname == null) return null;
		if (password == null) return null;
		String passwordHash = Util.calculateSha(password);
		BlogUser user = DAOProvider.getDAO().getBlogUser(nickname);
		if (user == null) return null;
		if (!user.getPasswordHash().equals(passwordHash)) return null;
		return user;
	}
	
	/**
	 * Sets session attributes when login process was successful.
	 * 
	 * @param req
	 *  	  request object used to set attributes
	 * @param user
	 *        logged user
	 */
	private void setSessionAttribs(HttpServletRequest req, BlogUser user) {
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());
	}
	
}
