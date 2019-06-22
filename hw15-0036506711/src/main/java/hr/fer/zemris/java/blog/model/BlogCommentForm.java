package hr.fer.zemris.java.blog.model;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.util.Util;

/**
 * Represents formular for comments.
 * 
 * @author Filip Husnjak
 */
public class BlogCommentForm extends Formular<BlogComment> {
	
	/** Message of the comment */
	private String message;
	
	/** Email of the user who wrote the comment */
	private String email;
	
	@Override
	public void fillRecord(BlogComment comment) {
		comment.setMessage(message);
		comment.setUsersEmail(email);
	}
	
	@Override
	public void fillFromRequest(HttpServletRequest req) {
		message = set(req.getParameter("message"));
		email = set(getEmail(req));
	}
	
	@Override
	public void fillFromRecord(BlogComment comment) {
		message = set(comment.getMessage());
		email = set(comment.getUsersEmail());
	}
	
	@Override
	public void validate() {
		errors.clear();
		if (email.isEmpty()) {
			errors.put("email", "Email name is obligatory!");
		} else if (!email.contains("@")) {
			errors.put("email", "Email is not valid!");
		}
	}
	
	/**
	 * Returns email of the current user if it exists, email from the request
	 * parameter is returned otherwise.
	 * 
	 * @param req
	 *        request object used to get email parameter
	 * @return email of the current user if it exists, email from the request
	 *         parameter is returned otherwise.
	 */
	private String getEmail(HttpServletRequest req) {
		BlogUser currentUser = Util.getCurrentUser(req);
		if (currentUser != null) return currentUser.getEmail();
		return req.getParameter("email");
	}

	public String getMessage() {
		return message;
	}

	public String getEmail() {
		return email;
	}
	
}
