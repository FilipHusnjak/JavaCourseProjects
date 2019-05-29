package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Represents exception that is thrown by {@code SmartScriptEngine} when error
 * occurs upon execution of a script.
 * 
 * @author Filip Husnjak
 */
public class SmartScriptEngineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code SmartScriptEngineException} without additional message.
	 */
	public SmartScriptEngineException() {
		super();
	}
	
	/**
	 * Constructs an {@code SmartScriptEngineException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public SmartScriptEngineException(String message) {
		super(message);
	}
	
}
