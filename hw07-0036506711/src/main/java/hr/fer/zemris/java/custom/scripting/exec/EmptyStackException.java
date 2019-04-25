package hr.fer.zemris.java.custom.scripting.exec;

/**
 * This class represents empty stack exception. It is thrown when accessing elements from empty stack.
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmptyStackException() {
		super();
	}
	
	public EmptyStackException(String message) {
		super(message);
	}
	
}
