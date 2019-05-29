package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents exception that is thrown by {@code Lexer} when expression is wrong.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptLexerException extends RuntimeException {

	private static final long serialVersionUID = 5936656131853783560L;

	/**
	 * Constructs an {@code LexerException} without additional message.
	 */
	public SmartScriptLexerException() {
		super();
	}
	
	/**
	 * Constructs an {@code LexerException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}
	
}
