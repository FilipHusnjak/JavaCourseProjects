package hr.fer.zemris.java.blog.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.BlogUserForm;
import hr.fer.zemris.java.util.Util;

/**
 * Servlet used to register new users.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7132307273205462902L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		BlogUser user = new BlogUser();
		BlogUserForm formular = new BlogUserForm();
		formular.fillFromRecord(user);
		dispatchToRegister(req, resp, formular);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String method = req.getParameter("method");
		if(!"Save".equals(method)) {
			resp.sendRedirect(req.getServletContext().getContextPath());
			return;
		}
		BlogUserForm formular = new BlogUserForm();
		Util.checkForm(req, formular);
		if (formular.hasErrors()) {
			dispatchToRegister(req, resp, formular);
			return;
		}
		BlogUser user = new BlogUser();
		formular.fillRecord(user);
		DAOProvider.getDAO().saveBlogUser(user);
		resp.sendRedirect(req.getServletContext().getContextPath());
	}
	
	/**
	 * Dispatches the request to the register page where user can fill out form.
	 * 
	 * @param req
	 *        request object used for dispatching the request
	 * @param resp
	 *        response object used for dispatching the request
	 * @param formular
	 *        formular used to get data from the user
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	private void dispatchToRegister(HttpServletRequest req, HttpServletResponse resp, 
			BlogUserForm formular) throws ServletException, IOException {
		Util.provideLoggedUserInfo(req);
		req.setAttribute("formular", formular);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
}
