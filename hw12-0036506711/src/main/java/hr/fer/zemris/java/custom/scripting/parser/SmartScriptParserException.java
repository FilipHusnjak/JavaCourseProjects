package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Represents exception that is thrown by {@code SmartScriptParserException} when expression is wrong.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code SmartScriptParserException} without additional message.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Constructs an {@code SmartScriptParserException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
	
}
