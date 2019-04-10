package hr.fer.zemris.java.hw05.db;

/**
 * Represents the filter which accepts single student record and returns {@code true} if
 * record satisfies certain conditions.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface IFilter {

	/**
	 * Returns true if the record satisfies condition specified by specific implementation
	 * of this filter.
	 * 
	 * @param record
	 *        record to be tested
	 * @return true if the record satisfies condition specified by specific implementation
	 *         of this filter
	 */
	boolean accepts(StudentRecord record);
	
}
