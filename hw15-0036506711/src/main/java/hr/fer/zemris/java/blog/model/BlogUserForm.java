package hr.fer.zemris.java.blog.model;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.util.Util;

/**
 * Represents formular for users.
 * 
 * @author Filip Husnjak
 */
public class BlogUserForm extends Formular<BlogUser> {
	
	/** First name of the user */
	private String firstName;

	/** Last name of the user */
	private String lastName;
	
	/** Nickname of the user */
	private String nick;

	/** Email of the user */
	private String email;
	
	/** Password hash of the user */
	private String passwordHash;

	@Override
	public void fillFromRequest(HttpServletRequest req) {
		firstName = set(req.getParameter("firstName"));
		lastName = set(req.getParameter("lastName"));
		nick = set(req.getParameter("nick"));
		email = set(req.getParameter("email"));
		passwordHash = Util.calculateSha(set(req.getParameter("password")));
	}
	
	@Override
	public void fillFromRecord(BlogUser user) {
		firstName = set(user.getFirstName());
		lastName = set(user.getLastName());
		nick = set(user.getNick());
		email = set(user.getEmail());
		passwordHash = "";
	}

	@Override
	public void fillRecord(BlogUser user) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setEmail(email);
		user.setPasswordHash(passwordHash);
	}
	
	@Override
	public void validate() {
		errors.clear();
		if(firstName.isEmpty()) {
			errors.put("firstName", "First name is obligatory!");
		}
		if(lastName.isEmpty()) {
			errors.put("lastName", "Last name is obligatory!");
		}
		if(nick.isEmpty()) {
			errors.put("nick", "Nickname is obligatory!");
		} else if (DAOProvider.getDAO().getBlogUser(nick) != null) {
			errors.put("nick", "Nickname already exists!");
		}
		if(email.isEmpty()) {
			errors.put("email", "Email is obligatory!");
		} else if (!email.contains("@")) {
			errors.put("email", "Email is not valid!");
		}
		if(passwordHash.isEmpty()) {
			errors.put("password", "Password is obligatory!");
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNick() {
		return nick;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
	
}
