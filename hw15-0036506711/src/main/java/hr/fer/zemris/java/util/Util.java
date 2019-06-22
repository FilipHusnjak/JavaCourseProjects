package hr.fer.zemris.java.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.model.Formular;

/**
 * Helper class that provides useful static methods for servlets.
 * 
 * @author Filip Husnjak
 */
public class Util {
	
	/**
	 * Algorithm used for message digesting
	 */
	private static final String ALGORITHM = "SHA-256";
	
	/**
	 * Prevents users from creating instances of this class
	 */
	private Util() {}
	
	/**
	 * Converts the given byte array into sequence of hexadecimal digits represented
	 * as string object which is then returned. Each byte will be converted into 2
	 * digits so the result String will have a length of {@code bytes.length * 2}.
	 * 
	 * @param bytes
	 *        byte array to be converted
	 * @return String representation of a result of a conversion
	 * @throws NullPointerException if the given array is {@code null}
	 */
	public static String byteToHex(byte[] bytes) {
		Objects.requireNonNull(bytes, "Given array cannot be null!");
		StringBuilder result = new StringBuilder();
		for (byte b: bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}
	
	/**
	 * Returns nickname of the current user.
	 * 
	 * @return nickname of the current user
	 */
	private static String getCurrentNick(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("current.user.nick");
	}

	/**
	 * Dispatches user to the error page with showing proper code.
	 * 
	 * @param code
	 *        error code
	 * @throws ServletException if there was an error executing this servlet
	 * @throws IOException if there was an error communicating with database
	 */
	public static void sendError(HttpServletRequest req, HttpServletResponse resp, int code) 
			throws ServletException, IOException {
		req.setAttribute("code", code);
		req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
	}
	
	/**
	 * Returns {@code true} if the given user is currently logged in.
	 *
	 * @param user
	 *        user to be checked
	 * @return {@code true} if the given user is currently logged in
	 */
	public static boolean checkSession(HttpServletRequest req, BlogUser user) {
		String currentNick = getCurrentNick(req);
		return user.getNick().equals(currentNick);
	}
	
	/**
	 * Returns current logged user, {@code null} is returned if there is no one
	 * logged yet.
	 * 
	 * @return current logged user, {@code null} is returned if there is no one
	 *         logged yet
	 */
	public static BlogUser getCurrentUser(HttpServletRequest req) {
		return DAOProvider.getDAO().getBlogUser(getCurrentNick(req));
	}

	/**
	 * Calculates and returns hash for the given password using proper algorithm.
	 * 
	 * @param password
	 *        password whose hash is to be calculated
	 * @return hash for the given password
	 */
	public static String calculateSha(String password) {
		if (password == null || password.isBlank()) return "";
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException ignorable) {}
		final byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
		sha.update(bytes, 0, bytes.length);
		return Util.byteToHex(sha.digest());
	}
	
	/**
	 * Sets attributes for the logged user so that jsp files can read it.
	 */
	public static void provideLoggedUserInfo(HttpServletRequest req) {
		req.setAttribute("someoneLogged", Util.getCurrentUser(req) != null);
		req.setAttribute("loggedUser", Util.getCurrentUser(req));
	}
	
	/**
	 * Returns list of parts of the given path. Each part should be splited with
	 * character '/'.
	 * 
	 * @param info
	 *        path to be splited
	 * @return list of parts of the given path
	 */
	public static List<String> getPathList(String info) {
		info = info.replaceAll("^/", "");
		List<String> path = new LinkedList<>();
		StringBuilder sb = new StringBuilder();
		for (char c: info.toCharArray()) {
			if (c == '/') {
				path.add(sb.toString());
				sb = new StringBuilder();
				continue;
			}
			sb.append(c);
		}
		path.add(sb.toString());
		return path;
	}
	
	/**
	 * Fill the form from the request and validates it.
	 * 
	 * @param req
	 *        request object used to fill form
	 * @param form
	 *        form which is filled and validated
	 */
	public static void checkForm(HttpServletRequest req, Formular<?> form) {
		form.fillFromRequest(req);
		form.validate();
	}
	
}
