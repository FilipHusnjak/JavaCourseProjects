package hr.fer.zemris.java.custom.collections;

/**
 * This class represents empty stack exception. It is thrown when accessing elements from empty stack.
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an {@code EmptyStackException} without additional message.
	 */
	public EmptyStackException() {
		super();
	}
	
	/**
	 * Constructs an {@code EmptyStackException} with specified additional message.
	 * 
	 * @param message
	 *        message to be written
	 */
	public EmptyStackException(String message) {
		super(message);
	}
	
}
