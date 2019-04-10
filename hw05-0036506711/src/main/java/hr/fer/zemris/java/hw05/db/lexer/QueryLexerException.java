package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Represents exception that is thrown by {@code QueryLexer} when expression is wrong.
 * 
 * @author Filip Husnjak
 */
public class QueryLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code QueryLexerException} without additional message.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Constructs an {@code QueryLexerException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public QueryLexerException(String message) {
		super(message);
	}
	
}
