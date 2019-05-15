package hr.fer.zemris.java.gui.layouts;

/**
 * Represents exception thrown by {@link CalcLayout} class if illegal action
 * is performed.
 * 
 * @author Filip Husnjak
 */
public class CalcLayoutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new {@link CalcLayoutException} without any additional message.
	 */
	public CalcLayoutException() {
		super();
	}
	
	/**
	 * Constructs new {@link CalcLayoutException} with specified additional message.
	 * 
	 * @param message
	 *        additional message to be written
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
	
}
