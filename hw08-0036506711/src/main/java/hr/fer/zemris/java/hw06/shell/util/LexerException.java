package hr.fer.zemris.java.hw06.shell.util;

/**
 * Represents exception that is thrown by {@code QueryLexer} when expression is wrong.
 * 
 * @author Filip Husnjak
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code QueryLexerException} without additional message.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Constructs an {@code QueryLexerException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public LexerException(String message) {
		super(message);
	}
	
}
