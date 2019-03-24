package hr.fer.zemris.java.custom.collections;

/**
 * Represents an operation that accepts a single input argument and returns no 
 * result.
 * 
 * @author Filip Husnjak
 */
public interface Processor {
	
	/**
	 * Performs this operation on the given argument.
	 * 
	 * @param value
	 *        the input value
	 */
	void process(Object value);
	
}
