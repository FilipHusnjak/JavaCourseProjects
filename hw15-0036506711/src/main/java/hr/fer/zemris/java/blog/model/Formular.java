package hr.fer.zemris.java.blog.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents abstract formular for checking user input.
 * 
 * @author Filip Husnjak
 *
 * @param <T>
 */
public abstract class Formular<T> {
	
	/** Map which maps error messages to the proper parameters */
	protected Map<String, String> errors = new HashMap<>();

	/**
	 * Fills this formular from the given request.
	 * 
	 * @param req
	 *        request used to fill formular
	 */
	public abstract void fillFromRequest(HttpServletRequest req);
	
	/**
	 * Fills this formular from the given record.
	 * 
	 * @param record
	 *        object used to fill this formular
	 */
	public abstract void fillFromRecord(T record);
	
	/**
	 * Fills the given record with data stored in this formular
	 * 
	 * @param record
	 *        record to be filled
	 */
	public abstract void fillRecord(T record);
	
	/**
	 * Validates the current formular and stores proper errors in a member map.
	 */
	public abstract void validate();
	
	/**
	 * Returns error message mapped to the specified key.
	 * 
	 * @param name
	 *        name of the error to be returned
	 * @return error message mapped to the specified key
	 */
	public String getError(String name) {
		return errors.get(name);
	}
	
	/**
	 * Returns {@code true} if there are errors in this formular.
	 * 
	 * @return {@code true} if there are errors in this formular
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Returns {@code true} if there is error mapped to the given key.
	 * 
	 * @param name
	 *        name of the error to be checked
	 * @return {@code true} if there is error mapped to the given key
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}
	
	/**
	 * Returns empty {@link String} if the given parameter is {@code null}, otherwise
	 * given parameter is returned
	 * 
	 * @param s
	 *        parameter to be checked
	 * @return empty {@link String} if the given parameter is {@code null}, otherwise
	 *         given parameter is returned
	 */
	protected String set(String s) {
		if(s==null) return "";
		return s.trim();
	}
	
}
