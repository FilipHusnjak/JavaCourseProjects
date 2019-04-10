package hr.fer.zemris.java.hw05.db;

/**
 * Functional interface which returns true if the comparison specified by each
 * implementation is satisfied.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Returns {@code true} if the comparison is satisfied.
	 * 
	 * @param value1
	 *        first parameter of the comparison
	 * @param value2
	 *        second parameter of the comparison
	 * @return {@code true} if the comparison is satisfied
	 */
	boolean satisfied(String value1, String value2);
	
}
