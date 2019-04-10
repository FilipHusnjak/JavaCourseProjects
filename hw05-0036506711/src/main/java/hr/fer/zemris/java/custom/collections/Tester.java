package hr.fer.zemris.java.custom.collections;

/**
 * Represents a tester which accepts single parameter which will be tested.
 * 
 * @author Filip Husnjak
 *
 * @param <T>
 *        type of the objects which will be tested
 */
@FunctionalInterface
public interface Tester<T> {
	
	/**
	 * Evaluates this test on the given argument. Returns the result
	 * of the test as boolean.
	 * 
	 * @param o
	 *        argument to be tested
	 * @return the result of the test as boolean.
	 */
	boolean test(T o);
	
}
