package hr.fer.zemris.java.hw05.db;

/**
 * Represents exception that is thrown by {@code QueryParser} when expression is wrong.
 * 
 * @author Filip Husnjak
 */
public class QueryParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code QueryParserException} without additional message.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Constructs an {@code QueryParserException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public QueryParserException(String message) {
		super(message);
	}
	
}
